# VinaClipAI Project Checklist

Tài liệu này là checklist điều phối triển khai cho nền tảng AI hỗ trợ dựng clip tuyên truyền. Mỗi hạng mục phải được cập nhật khi hoàn thành, và mỗi task hoàn chỉnh phải được commit riêng để dễ truy vết.

Nguồn yêu cầu bắt buộc:

- `Dac-ta-san-pham-AI-dung-clip-tuyen-truyen.md`: đặc tả sản phẩm, nghiệp vụ, bảo mật, kiến trúc và phạm vi MVP.
- `DESIGN.md`: định hướng UI theo Carbon Design System, IBM Plex Sans, giao diện phẳng, square corners, hairline borders.

## 1. Nguyên tắc triển khai bắt buộc

- [ ] Không phát triển sản phẩm cuối như một demo nhập prompt rồi xuất video.
- [ ] MoneyPrinterTurbo chỉ đóng vai trò video/AI worker hoặc engine tham chiếu.
- [ ] Frontend production phải tách khỏi Streamlit hiện tại.
- [ ] Backend nghiệp vụ production phải tách khỏi FastAPI hiện tại.
- [ ] Mọi clip chính thức phải đi qua kiểm tra và phê duyệt.
- [ ] Không cho xuất bản bản nháp hoặc bản chưa duyệt qua kênh chính thức.
- [ ] Mỗi lần render phải gắn với đúng script version và storyboard version.
- [ ] Mọi thay đổi trạng thái nghiệp vụ quan trọng phải ghi audit log.
- [ ] Không đưa secret, API key, token, dữ liệu thật hoặc media nhạy cảm vào repository.
- [ ] Mỗi task hoàn thành phải kiểm thử phù hợp, cập nhật checklist và commit riêng.

## 2. Kiến trúc tổng thể

- [ ] Tách hệ thống thành 3 khối chính:
  - [ ] `frontend-app`: React + TypeScript.
  - [ ] `core-backend`: Spring Boot.
  - [ ] `video-ai-worker`: Python, kế thừa phần phù hợp từ MoneyPrinterTurbo.
- [ ] Frontend chỉ gọi Core Backend, không gọi trực tiếp Video Worker.
- [ ] Core Backend điều phối nghiệp vụ, workflow, phân quyền, audit và job.
- [ ] Video Worker chỉ xử lý AI/media/render qua API nội bộ hoặc queue.
- [x] Object Storage dùng MinIO/S3-compatible cho ảnh, video, audio, subtitle, thumbnail và output.
- [x] PostgreSQL là database nghiệp vụ chính.
- [x] Redis dùng cho queue/cache/progress/lock/retry nếu phù hợp.
- [x] Docker Compose quản lý các service local/dev.
- [ ] Makefile cung cấp lệnh chạy, build, test, migrate, logs, clean theo chuẩn thống nhất.
- [ ] Viết ADR khi có quyết định kỹ thuật khác đặc tả.

## 3. Cấu trúc repository mục tiêu

- [x] Tạo thư mục `frontend/` cho React + TypeScript.
- [x] Tạo thư mục `backend/` cho Spring Boot.
- [ ] Tái định vị phần MoneyPrinterTurbo hiện tại thành worker hoặc module Python có ranh giới rõ.
- [x] Tạo `infra/` hoặc `deploy/` cho Docker, compose, scripts triển khai.
- [x] Tạo `docs/architecture/` cho tài liệu kiến trúc.
- [x] Tạo `docs/api/` cho OpenAPI/API contract.
- [x] Tạo `docs/runbooks/` cho vận hành, backup, restore, incident.
- [x] Tạo `docs/security/` cho threat model và chính sách dữ liệu.
- [x] Tạo `docs/testing/` cho test strategy.

## 4. Backend Spring Boot

### 4.1. Chuẩn code và phân tầng

- [ ] Backend dùng Java + Spring Boot.
- [ ] Tuân thủ SOLID, dependency inversion và separation of concerns.
- [ ] Controller chỉ xử lý HTTP request/response, validation entrypoint và gọi service.
- [ ] Service interface định nghĩa nghiệp vụ.
- [ ] Service implementation chứa logic nghiệp vụ, transaction, kiểm tra quyền, state transition.
- [ ] Repository interface định nghĩa thao tác dữ liệu.
- [ ] Repository implementation hoặc Spring Data adapter được tách khỏi service.
- [ ] DTO request tách khỏi entity database.
- [ ] DTO response tách khỏi entity database.
- [ ] Mapper tách riêng để chuyển đổi entity/domain/DTO.
- [ ] Exception handling tập trung qua global exception handler.
- [ ] Validation dùng annotation và validator riêng cho nghiệp vụ phức tạp.
- [ ] Không trả entity JPA trực tiếp ra API.
- [ ] Không để controller gọi repository trực tiếp.
- [ ] Không để service phụ thuộc trực tiếp vào provider AI/TTS/STT cụ thể.

### 4.2. Package convention đề xuất

- [ ] `controller`: REST controllers.
- [ ] `service`: service interfaces.
- [ ] `service.impl`: service implementations.
- [ ] `repository`: repository interfaces.
- [ ] `repository.impl`: custom repository implementations/adapters khi cần.
- [ ] `entity`: JPA entities.
- [ ] `dto.request`: request DTOs.
- [ ] `dto.response`: response DTOs.
- [ ] `mapper`: entity/domain/DTO mappers.
- [ ] `config`: Spring/security/storage/queue configs.
- [ ] `security`: auth, RBAC, permission evaluation.
- [ ] `exception`: domain exceptions and handlers.
- [ ] `audit`: audit logging components.
- [ ] `workflow`: state machine/state transition policies.
- [ ] `integration`: clients gọi Video Worker, AI providers, object storage.

### 4.3. Database PostgreSQL

- [ ] Dùng PostgreSQL cho dữ liệu nghiệp vụ.
- [ ] Dùng migration tool như Flyway hoặc Liquibase.
- [ ] Mọi schema change phải có migration.
- [ ] Index cho các trường tìm kiếm chính: organization, status, created_at, activity type, owner, reviewer, approver.
- [ ] Không lưu file media trực tiếp trong database; chỉ lưu metadata và object key.
- [ ] Có soft delete cho dữ liệu nghiệp vụ cần khôi phục.
- [ ] Có retention policy cho media, log và bản nháp.
- [x] Database local chạy bằng Docker để kết nối qua DBeaver.
- [x] README phải ghi rõ host, port, database, username, password dev.
- [ ] Không commit credential production.

### 4.4. Auth, RBAC và organization scope

- [ ] Đăng nhập.
- [ ] Đăng xuất.
- [ ] Refresh/revoke session hoặc token.
- [ ] Đổi mật khẩu.
- [ ] Khôi phục tài khoản theo chính sách.
- [ ] Quản lý user.
- [ ] Tạm khóa user.
- [ ] Thu hồi phiên đăng nhập.
- [ ] Quản lý organization.
- [ ] Quản lý role.
- [ ] Quản lý permission.
- [ ] Phân quyền theo role.
- [ ] Phân quyền theo phạm vi dữ liệu của đơn vị.
- [ ] Audit login/logout/failure.
- [ ] MFA cho vai trò quan trọng ở giai đoạn phù hợp.
- [ ] Chính sách tách nhiệm vụ: một người không được tự tạo, tự kiểm tra và tự phê duyệt khi policy bật.

## 5. Domain nghiệp vụ P0

### 5.1. Activity

- [ ] Tạo hồ sơ hoạt động.
- [ ] Lưu nháp hồ sơ.
- [ ] Chỉnh sửa hồ sơ.
- [ ] Sao chép hồ sơ.
- [ ] Tìm kiếm hồ sơ.
- [ ] Lưu trữ hồ sơ.
- [ ] Theo dõi trạng thái hồ sơ.
- [ ] Theo dõi lịch sử thay đổi.
- [ ] Trường bắt buộc: tên, loại hoạt động, thời gian, địa điểm, đơn vị, mục tiêu, kết quả, thông điệp, nguồn thông tin, người phụ trách.
- [ ] Đánh dấu dữ kiện đã được xác nhận.
- [ ] Đính kèm tài liệu nguồn.
- [ ] Gắn nhãn chủ đề.

### 5.2. Asset library

- [ ] Upload một hoặc nhiều file.
- [ ] Upload kéo-thả từ frontend.
- [ ] Hiển thị tiến độ upload.
- [ ] Retry upload khi lỗi.
- [ ] Kiểm tra định dạng JPG/PNG/WEBP/MP4/MOV.
- [ ] Kiểm tra dung lượng.
- [ ] Kiểm tra codec/độ phân giải/tỷ lệ/khả năng giải mã.
- [ ] Tạo thumbnail ảnh/video.
- [ ] Tạo proxy video nhẹ.
- [ ] Tạo waveform âm thanh.
- [ ] Phát hiện file trùng bằng hash.
- [ ] Cảnh báo chất lượng thấp.
- [ ] Lưu metadata: mô tả, người chụp, địa điểm, thời gian, quyền sử dụng, trạng thái đồng ý.
- [ ] Phân loại theo hồ sơ, sự kiện, tổ chức, năm, nhãn.
- [ ] Xóa mềm.
- [ ] Khôi phục theo quyền.

### 5.3. Script và AI content

- [ ] Tạo script thủ công.
- [ ] AI đề xuất tiêu đề.
- [ ] AI đề xuất thông điệp.
- [ ] AI tạo phần mở đầu, thân bài, lời kết.
- [ ] Tạo nhiều phiên bản theo 30s/60s/90s/tùy chỉnh.
- [ ] Chọn phong cách: trang trọng, cộng đồng, bản tin, hướng dẫn, tóm tắt sự kiện.
- [ ] Dẫn chiếu dữ kiện quan trọng về trường dữ liệu hoặc tài liệu nguồn.
- [ ] Đánh dấu tên, ngày, số liệu, chức danh cần xác nhận.
- [ ] Kiểm tra chính tả.
- [ ] Cảnh báo câu quá dài, từ khó hiểu, nội dung lặp.
- [ ] Cảnh báo trường còn thiếu.
- [ ] Không tự phát minh số liệu.
- [ ] Lưu prompt/model/params/kết quả đã che secret để truy vết.
- [ ] So sánh script versions.
- [ ] Duyệt script trước khi dựng.

### 5.4. Storyboard

- [ ] Tạo storyboard từ script đã duyệt.
- [ ] Chia cảnh với thời lượng dự kiến.
- [ ] Đề xuất ảnh/video từ thư viện nội bộ.
- [ ] Không tự ý lấy tư liệu ngoài.
- [ ] Kéo-thả sắp xếp cảnh.
- [ ] Gán asset cho từng scene.
- [ ] Chọn layout: toàn màn hình, ảnh trong khung, chia đôi, tiêu đề, số liệu nổi bật, quote.
- [ ] Cắt đầu/cuối video.
- [ ] Chọn đoạn sử dụng.
- [ ] Điều chỉnh tốc độ trong giới hạn.
- [ ] Hiệu ứng pan/zoom nhẹ cho ảnh.
- [ ] Chuyển cảnh.
- [ ] Khóa cảnh đã kiểm tra.
- [ ] Preview storyboard trước render.

### 5.5. Voiceover, subtitle, music, brand kit

- [ ] Adapter TTS tiếng Việt.
- [ ] Cấu hình giọng nam/nữ/vùng giọng nếu provider hỗ trợ.
- [ ] Nghe thử từng đoạn.
- [ ] Điều chỉnh tốc độ/cao độ/khoảng nghỉ.
- [ ] Từ điển phát âm tên người, địa danh, thuật ngữ.
- [ ] Cho phép dùng bản thu âm cán bộ thay TTS.
- [ ] Không triển khai voice cloning mặc định.
- [ ] Tạo subtitle từ TTS timestamp.
- [ ] Tạo subtitle từ STT/faster-whisper ở giai đoạn phù hợp.
- [ ] Editor subtitle theo timeline.
- [ ] Xuất SRT/VTT.
- [ ] Nhúng subtitle vào video.
- [ ] Kiểm tra dòng phụ đề, thời gian hiển thị, chồng lấn.
- [ ] Quản lý logo, màu, font, intro, outro, lower-third, watermark, câu kết.
- [ ] Versioning cho brand kit/template.
- [ ] Khóa thành phần nhận diện bắt buộc.
- [ ] Thư viện nhạc nội bộ có giấy phép, nguồn, phạm vi sử dụng.
- [ ] Cảnh báo nhạc/tư liệu thiếu quyền sử dụng.

### 5.6. Render job

- [ ] Render preview độ phân giải thấp.
- [ ] Render final chất lượng cao.
- [ ] Hỗ trợ 9:16.
- [ ] Hỗ trợ 16:9.
- [ ] Hỗ trợ 1:1 khi cần.
- [ ] Cấu hình 720p/1080p/bitrate.
- [ ] Job queue bất đồng bộ.
- [ ] Progress percentage.
- [ ] Hủy job.
- [ ] Retry job có kiểm soát.
- [ ] Idempotency cho render/publish ở mức phù hợp.
- [ ] Cleanup file tạm theo chính sách.
- [ ] Log kỹ thuật đủ truy vết.
- [ ] QA output: phát được, có âm thanh, đúng thời lượng, không thiếu cảnh, không lỗi phụ đề.
- [ ] Watermark `BẢN NHÁP` cho preview chưa duyệt nếu policy yêu cầu.

### 5.7. Review và approval

- [ ] Gửi version đến người kiểm tra.
- [ ] Gửi version đến người phê duyệt.
- [ ] Comment theo timestamp trên video.
- [ ] Checklist kiểm tra: tên, chức danh, ngày, địa điểm, số liệu, hình ảnh, phụ đề, logo, bản quyền, dữ liệu cá nhân.
- [ ] Chấp thuận.
- [ ] Yêu cầu sửa.
- [ ] Từ chối.
- [ ] Bắt buộc ghi lý do khi trả lại/từ chối.
- [ ] Không sửa âm thầm sau khi duyệt.
- [ ] Thay đổi sau duyệt phải tạo version mới.
- [ ] Lưu chữ ký nghiệp vụ: account, time, system trace.

### 5.8. Publication và archive

- [ ] Tải video.
- [ ] Tải subtitle.
- [ ] Tải thumbnail.
- [ ] Tải nội dung mô tả đăng kèm.
- [ ] Chỉ cho publish khi đã duyệt.
- [ ] Ghi nhận kênh, thời điểm, người thực hiện, URL/mã bài đăng.
- [ ] Retry publish an toàn, tránh đăng trùng.
- [ ] Giai đoạn đầu ưu tiên tải xuống thủ công.
- [ ] Tích hợp Zalo OA/Facebook chỉ khi API và quyền hợp lệ.
- [ ] Lưu hồ sơ, nguồn, script, storyboard, draft, approved version, published version.
- [ ] Tìm kiếm theo từ khóa, thời gian, đơn vị, loại hoạt động, trạng thái, người phụ trách.
- [ ] Xuất hồ sơ kiểm toán khi cần.

## 6. Frontend React + TypeScript

### 6.1. Stack và chuẩn code

- [ ] Dùng React + TypeScript.
- [ ] Dùng routing rõ ràng cho các module nghiệp vụ.
- [ ] API client tách khỏi component.
- [ ] State management chọn theo nhu cầu, không over-engineer.
- [ ] Form validation client-side và hiển thị lỗi tiếng Việt.
- [ ] Component nhỏ, có trách nhiệm rõ.
- [ ] Không hard-code role/permission trong UI mà phải dựa trên dữ liệu từ backend.
- [ ] Toàn bộ giao diện, lỗi, trợ giúp và trạng thái bằng tiếng Việt.

### 6.2. UI theo DESIGN.md

- [ ] Dùng IBM Plex Sans.
- [ ] Áp dụng Carbon-like design.
- [ ] Nền chính trắng, surface phụ light gray.
- [ ] Text chính charcoal.
- [ ] IBM Blue `#0f62fe` làm accent duy nhất.
- [ ] Button/card/input/container dùng góc vuông `0px`.
- [ ] Dùng border 1px hairline, hạn chế shadow.
- [ ] Body letter-spacing `0.16px`.
- [ ] Display typography weight 300.
- [ ] Grid theo 4px spacing system.
- [ ] Responsive desktop/tablet/mobile.
- [ ] Touch target tối thiểu 48px.
- [ ] Không dùng pill button.
- [ ] Không thêm gradient/shadow ngoài phạm vi thiết kế.

### 6.3. Màn hình P0

- [ ] Login.
- [ ] Dashboard tổng quan.
- [ ] Quản lý người dùng.
- [ ] Quản lý đơn vị.
- [ ] Quản lý vai trò/quyền.
- [ ] Danh sách hồ sơ hoạt động.
- [ ] Tạo/sửa hồ sơ hoạt động.
- [ ] Upload và quản lý asset.
- [ ] AI script assistant.
- [ ] Script editor/version compare.
- [ ] Script review.
- [ ] Storyboard editor cơ bản.
- [ ] Voiceover/subtitle/music settings.
- [ ] Render job progress.
- [ ] Video preview.
- [ ] Review comments theo timestamp.
- [ ] Approval checklist.
- [ ] Download/export.
- [ ] Audit/history view.

## 7. Video/AI Worker Python

- [ ] Xác định phần MoneyPrinterTurbo giữ lại.
- [ ] Tách worker API khỏi WebUI Streamlit.
- [ ] Bọc render preview.
- [ ] Bọc render final.
- [ ] Bọc TTS.
- [ ] Bọc STT/subtitle.
- [ ] Bọc media normalization.
- [ ] Bọc thumbnail/proxy/waveform.
- [ ] Chuẩn hóa job request/response contract.
- [ ] Trả progress/error có cấu trúc.
- [ ] Không để worker quyết định quyền nghiệp vụ.
- [ ] Không để worker publish trực tiếp.
- [ ] Không để worker lưu secret vào log.
- [ ] Giữ notice giấy phép MoneyPrinterTurbo/MoviePy/FFmpeg/faster-whisper.

## 8. Docker, Makefile và môi trường local

- [x] `docker-compose.yml` chạy PostgreSQL.
- [x] `docker-compose.yml` chạy Redis.
- [x] `docker-compose.yml` chạy MinIO.
- [ ] `docker-compose.yml` chạy backend.
- [ ] `docker-compose.yml` chạy frontend.
- [ ] `docker-compose.yml` chạy video worker.
- [x] Healthcheck cho service quan trọng.
- [x] Volume riêng cho PostgreSQL/MinIO.
- [x] `.env.example` đầy đủ biến môi trường dev.
- [x] Không commit `.env`.
- [x] Makefile có `make up`.
- [x] Makefile có `make down`.
- [x] Makefile có `make logs`.
- [x] Makefile có `make ps`.
- [ ] Makefile có `make backend-test`.
- [ ] Makefile có `make frontend-test`.
- [ ] Makefile có `make worker-test`.
- [x] Makefile có `make test`.
- [ ] Makefile có `make migrate`.
- [ ] Makefile có `make seed`.
- [ ] Makefile có `make clean` an toàn, không xóa dữ liệu production.
- [x] README hướng dẫn kết nối PostgreSQL bằng DBeaver.

## 9. Bảo mật, dữ liệu và tuân thủ

- [ ] Phân loại dữ liệu: công khai, nội bộ, cá nhân, nhạy cảm/hạn chế.
- [ ] Thu thập dữ liệu tối thiểu đúng mục đích.
- [ ] Mã hóa khi truyền.
- [ ] Mã hóa khi lưu đối với dữ liệu cần bảo vệ.
- [ ] Tách database, object storage và secret.
- [ ] Cấp quyền theo nguyên tắc tối thiểu.
- [ ] Không ghi API key/token/dữ liệu nhạy cảm vào log.
- [ ] Có retention policy.
- [ ] Có quy trình xóa mềm/xóa vĩnh viễn.
- [ ] Cấu hình rõ dữ liệu nào được gửi đến provider AI bên ngoài.
- [ ] Threat model trước production.
- [ ] Rà soát pháp lý và an toàn thông tin trước vận hành chính thức.
- [ ] Không dùng AI để giả mạo khuôn mặt/giọng nói/phát biểu.
- [ ] Không tự động đăng khi chưa phê duyệt.

## 10. Quan sát hệ thống và vận hành

- [ ] Structured logging.
- [ ] Correlation ID cho request/job.
- [ ] Metrics service health.
- [ ] Metrics queue.
- [ ] Metrics render.
- [ ] Dashboard worker/job/storage.
- [ ] Alert lỗi render.
- [ ] Alert lỗi queue.
- [ ] Alert dung lượng storage.
- [ ] Backup PostgreSQL.
- [ ] Backup MinIO/object storage theo chính sách.
- [ ] Kiểm thử restore.
- [ ] Runbook triển khai.
- [ ] Runbook backup/restore.
- [ ] Runbook incident.
- [ ] Migration rollback strategy.

## 11. Kiểm thử

- [ ] Unit test backend service.
- [ ] Unit test backend validator.
- [ ] Unit test backend mapper.
- [ ] Integration test repository/database.
- [ ] Integration test API auth/RBAC.
- [ ] Integration test workflow transition.
- [ ] Integration test audit log.
- [ ] Contract test backend-worker.
- [ ] Worker test render preview sample.
- [ ] Worker test subtitle/voiceover.
- [ ] Frontend unit/component test.
- [ ] Frontend integration test form/upload/workflow.
- [ ] E2E test luồng P0 từ hồ sơ đến video duyệt.
- [ ] Security test quyền truy cập ngoài phạm vi.
- [ ] Test không mất dấu tiếng Việt.
- [ ] Test secret không xuất hiện trong response/log.

## 12. Tài liệu kỹ thuật cần tạo

- [ ] `docs/01-product-requirements.md`.
- [ ] `docs/02-system-architecture.md`.
- [ ] `docs/03-data-model.md`.
- [ ] `docs/04-api-contract.yaml`.
- [ ] `docs/05-security-threat-model.md`.
- [ ] `docs/06-media-pipeline.md`.
- [ ] `docs/07-deployment-runbook.md`.
- [ ] `docs/08-test-strategy.md`.
- [ ] `docs/09-open-source-notices.md`.
- [ ] `docs/10-pilot-plan.md`.

## 13. Lộ trình triển khai

### Phase 0: Chuẩn hóa repo và tài liệu

- [x] Checklist tổng thể.
- [x] README dự án VinaClipAI.
- [x] Quyết định kiến trúc hybrid.
- [x] Quy ước commit/task.
- [x] Tạo cấu trúc thư mục mục tiêu.

### Phase 1: Nền tảng hạ tầng và backend core

- [x] Docker Compose PostgreSQL/Redis/MinIO.
- [x] Makefile.
- [ ] Spring Boot scaffold.
- [ ] Migration baseline.
- [ ] Auth/RBAC.
- [ ] Organization/User/Role/Permission.
- [ ] Audit log.

### Phase 2: Activity, asset và script

- [ ] Activity CRUD/workflow.
- [ ] Asset metadata/upload.
- [ ] Object storage integration.
- [ ] Script/versioning.
- [ ] AI content adapter contract.
- [ ] Script review.

### Phase 3: Storyboard và worker integration

- [ ] Storyboard/scene model.
- [ ] Worker API contract.
- [ ] Preview render.
- [ ] Voiceover/subtitle/music/brand kit basics.
- [ ] Job queue/progress/retry.

### Phase 4: Review, approval và final render

- [ ] Video review.
- [ ] Timestamp comments.
- [ ] Approval checklist.
- [ ] Final render từ approved version.
- [ ] Download/export/archive.

### Phase 5: Frontend production

- [ ] React app shell theo DESIGN.md.
- [ ] Auth/layout/navigation.
- [ ] Activity/asset/script/storyboard/review screens.
- [ ] Render progress/video preview.
- [ ] Dashboard cơ bản.

### Phase 6: Hardening và thí điểm

- [ ] Backup/restore.
- [ ] Monitoring/logging.
- [ ] Security threat model.
- [ ] E2E test P0.
- [ ] Pilot runbook.
- [ ] Training/SOP.

## 14. Quy tắc commit

- [ ] Mỗi task hoàn chỉnh commit riêng.
- [ ] Commit message dùng prefix rõ: `docs:`, `feat:`, `fix:`, `test:`, `refactor:`, `chore:`, `infra:`.
- [ ] Không commit khi test/verification liên quan chưa chạy.
- [ ] Không commit file chứa secret hoặc dữ liệu thật.
- [ ] Sau khi hoàn thành task, cập nhật checkbox tương ứng nếu task đó thực sự đạt.

## 15. Definition of Done cho mỗi task

- [ ] Code/tài liệu đúng phạm vi task.
- [ ] Không phá vỡ yêu cầu trong đặc tả.
- [ ] Có test hoặc verification phù hợp.
- [ ] Có hướng dẫn chạy nếu task ảnh hưởng vận hành.
- [ ] Checklist được cập nhật.
- [ ] `git status` chỉ chứa thay đổi thuộc task.
- [ ] Commit riêng đã được tạo.
