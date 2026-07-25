# VinaClipAI

Nền tảng AI hỗ trợ dựng clip tuyên truyền tiếng Việt, định hướng vận hành thực tế cho Ủy ban MTTQ Việt Nam cấp phường và các tổ chức thành viên.

Repo này được fork/phát triển từ nền tảng mã nguồn mở MoneyPrinterTurbo. Từ thời điểm tái định hướng, MoneyPrinterTurbo không còn được xem là toàn bộ sản phẩm cuối; nó được giữ làm nền tham chiếu cho video/AI worker.

## Mục tiêu sản phẩm

VinaClipAI không phải công cụ demo tạo video tự động. Sản phẩm hướng đến quy trình số khép kín:

```text
Tạo hồ sơ hoạt động
  -> tải và chuẩn hóa tư liệu
  -> AI hỗ trợ tạo kịch bản có dẫn chiếu nguồn
  -> kiểm tra và duyệt kịch bản
  -> tạo storyboard
  -> render bản nháp
  -> kiểm tra video
  -> phê duyệt
  -> render chính thức
  -> phân phối và lưu trữ
```

Nguyên tắc bắt buộc: AI chỉ là trợ lý; con người chịu trách nhiệm kiểm tra và phê duyệt cuối cùng.

## Tài liệu nền

- `Dac-ta-san-pham-AI-dung-clip-tuyen-truyen.md`: đặc tả sản phẩm, nghiệp vụ, bảo mật, kiến trúc và phạm vi MVP.
- `DESIGN.md`: định hướng UI theo Carbon Design System.
- `PROJECT_CHECKLIST.md`: checklist triển khai từ cơ bản đến nâng cao.

Mọi quyết định kiến trúc, kế hoạch hoặc code mới phải bám theo các tài liệu trên.

## Kiến trúc đã chốt

Hướng triển khai là hybrid: giữ phần mạnh của MoneyPrinterTurbo cho media pipeline, đồng thời xây mới frontend và backend nghiệp vụ.

```text
React + TypeScript Frontend
        |
        v
Spring Boot Core Backend
        |
        v
Queue / Internal API
        |
        v
Python Video/AI Worker
        |
        v
PostgreSQL / MinIO / Redis
```

### Frontend

- Công nghệ mục tiêu: React + TypeScript.
- Streamlit hiện tại của MoneyPrinterTurbo không phải frontend production.
- UI phải bám `DESIGN.md`:
  - IBM Plex Sans.
  - Carbon-like layout.
  - Nền trắng, surface xám nhạt, text charcoal.
  - IBM Blue `#0f62fe` làm accent chính.
  - Button/card/input/container vuông góc `0px`.
  - Hairline border 1px, hạn chế shadow.
  - Giao diện tiếng Việt.

### Core Backend

- Công nghệ mục tiêu: Spring Boot.
- Database: PostgreSQL.
- Backend phải viết theo hướng dễ quản lý và tuân thủ SOLID.
- Phân tầng bắt buộc:
  - Controller.
  - Service interface.
  - Service implementation.
  - Repository interface.
  - Repository implementation/custom adapter khi cần.
  - DTO request.
  - DTO response.
  - Entity.
  - Mapper.
  - Global exception handler.
  - Security/RBAC.
  - Audit/workflow.

Controller không được gọi repository trực tiếp. API không được trả entity database trực tiếp.

### Video/AI Worker

- Công nghệ hiện tại: Python, FastAPI/Uvicorn, MoviePy/FFmpeg, TTS/STT adapters.
- Vai trò mục tiêu:
  - render preview;
  - render final;
  - tạo voiceover;
  - tạo subtitle;
  - chuẩn hóa media;
  - tạo thumbnail/proxy/waveform.
- Worker không quyết định quyền nghiệp vụ, không tự publish, không giữ workflow duyệt.

## Database và công cụ quản trị

Database nghiệp vụ chính là PostgreSQL.

Môi trường local/dev sẽ chạy PostgreSQL bằng Docker Compose để có thể kết nối bằng DBeaver.

Thông tin kết nối dev dự kiến:

| Trường | Giá trị |
|---|---|
| Host | `localhost` |
| Port | `5432` |
| Database | `vinaclipai` |
| Username | `vinaclipai` |
| Password | lấy từ `.env` local |

Không commit credential production. File `.env.example` sẽ dùng để mô tả biến môi trường cần thiết.

## Docker services mục tiêu

Local development phải được chuẩn hóa bằng Docker.

Các service mục tiêu:

- `postgres`: PostgreSQL database.
- `redis`: queue/cache/progress/lock.
- `minio`: object storage S3-compatible.
- `backend`: Spring Boot core backend.
- `frontend`: React frontend.
- `video-worker`: Python Video/AI worker.

## Makefile mục tiêu

Makefile sẽ là entrypoint thao tác chính cho developer.

Các lệnh mục tiêu:

```bash
make up              # chạy toàn bộ local services
make down            # dừng services
make ps              # xem trạng thái services
make logs            # xem logs
make migrate         # chạy database migration
make seed            # tạo dữ liệu mẫu dev
make backend-test    # test backend
make frontend-test   # test frontend
make worker-test     # test video worker
make test            # chạy toàn bộ test phù hợp
```

Các lệnh destructive như clean/xóa dữ liệu phải được thiết kế an toàn, rõ scope, không động đến dữ liệu production.

## Phạm vi MVP vận hành thật

MVP không đồng nghĩa với demo. MVP phải đủ để chạy thí điểm có kiểm soát tại một đơn vị.

P0 bắt buộc:

- Đăng nhập, RBAC, quản lý đơn vị và audit cơ bản.
- Hồ sơ hoạt động.
- Thư viện tư liệu cục bộ.
- Tạo/chỉnh/duyệt kịch bản tiếng Việt.
- Storyboard cơ bản từ ảnh/video nội bộ.
- TTS tiếng Việt, phụ đề, nhạc và bộ nhận diện.
- Render preview/final 9:16 và 16:9 bằng hàng đợi.
- Kiểm tra, nhận xét, yêu cầu sửa và phê duyệt.
- Lưu phiên bản, tải đầu ra và lịch sử.
- Quản lý secret, backup, log, giám sát và tài liệu vận hành.

## Quy trình nghiệp vụ bắt buộc

Trạng thái nghiệp vụ chính:

- `DRAFT`
- `READY_FOR_SCRIPT`
- `SCRIPT_REVIEW`
- `SCRIPT_APPROVED`
- `STORYBOARD_EDITING`
- `RENDERING_PREVIEW`
- `VIDEO_REVIEW`
- `CHANGES_REQUESTED`
- `APPROVED`
- `RENDERING_FINAL`
- `READY_TO_PUBLISH`
- `PUBLISHED`
- `ARCHIVED`
- `FAILED`

Kiểm soát bắt buộc:

- Bản chưa duyệt không được xuất bản chính thức.
- Render final chỉ được tạo từ version đã phê duyệt.
- Thay đổi sau phê duyệt phải tạo version mới và duyệt lại.
- Mọi thao tác phê duyệt/từ chối/yêu cầu sửa phải ghi audit log.

## Bảo mật và dữ liệu

Yêu cầu bắt buộc:

- Phân loại dữ liệu: công khai, nội bộ, dữ liệu cá nhân, nhạy cảm/hạn chế.
- Không gửi dữ liệu nhạy cảm đến AI provider bên ngoài nếu chưa được cấu hình và cho phép.
- Không ghi API key, token hoặc dữ liệu nhạy cảm vào log.
- Secret phải được quản lý qua biến môi trường hoặc secret manager.
- Quyền truy cập theo vai trò và phạm vi đơn vị.
- Có soft delete, retention policy và quy trình xóa vĩnh viễn có thẩm quyền.
- Có backup, restore test và runbook vận hành trước production.

## Quy tắc phát triển

- Mỗi module phải có user story, acceptance criteria, API contract và migration nếu có dữ liệu.
- Mỗi task hoàn chỉnh phải được commit riêng.
- Không commit secret, dữ liệu thật hoặc media nhạy cảm.
- Không tuyên bố hoàn thành nếu chưa chạy verification phù hợp.
- Dependency mới phải được kiểm tra giấy phép.
- Thay đổi khác đặc tả phải có ADR.
- Checklist trong `PROJECT_CHECKLIST.md` phải được cập nhật theo tiến độ thực tế.

Commit message nên dùng prefix:

- `docs:`
- `feat:`
- `fix:`
- `test:`
- `refactor:`
- `infra:`
- `chore:`

## Trạng thái repo hiện tại

Hiện tại repo vẫn chứa code MoneyPrinterTurbo gốc:

- Backend/API hiện tại: Python + FastAPI.
- WebUI hiện tại: Python + Streamlit.
- Media pipeline: Python + MoviePy/FFmpeg/TTS/STT.

Đây là nền để tách thành `video-ai-worker`. Frontend React và backend Spring Boot sẽ được bổ sung theo các phase trong `PROJECT_CHECKLIST.md`.

## Upstream và giấy phép

MoneyPrinterTurbo upstream:

- Repository: <https://github.com/harry0703/MoneyPrinterTurbo>
- License: MIT

Khi triển khai VinaClipAI, phải giữ thông báo bản quyền và lập danh mục giấy phép cho toàn bộ dependency/media theo `docs/09-open-source-notices.md`.
