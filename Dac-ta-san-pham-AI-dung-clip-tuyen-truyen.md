**ĐẶC TẢ ĐỊNH HƯỚNG SẢN PHẨM**

**NỀN TẢNG AI HỖ TRỢ  
DỰNG CLIP TUYÊN TRUYỀN**

**Phát triển trên nền tảng mã nguồn mở MoneyPrinterTurbo**

**Định hướng:** Sản phẩm vận hành thực tế cho người Việt Nam, ưu tiên nghiệp vụ tuyên truyền tại Ủy ban MTTQ Việt Nam cấp phường và các tổ chức thành viên; không xây dựng theo phạm vi demo.

| **Thông tin**     | **Nội dung**                                                                  |
| ----------------- | ----------------------------------------------------------------------------- |
| Tên tài liệu      | Đặc tả bối cảnh, mục tiêu, phạm vi và chức năng sản phẩm                      |
| Phiên bản         | 1.0                                                                           |
| Ngôn ngữ sản phẩm | Tiếng Việt                                                                    |
| Đối tượng đọc     | Product Owner, Business Analyst, kiến trúc sư, lập trình viên và coding agent |
| Trạng thái        | Tài liệu định hướng phát triển chính thức                                     |
| Ngày lập          | 25/07/2026                                                                    |

**_Tài liệu này là nguồn yêu cầu nền tảng. Agent phải đọc toàn bộ trước khi đề xuất kiến trúc, lập kế hoạch hoặc viết mã._**

# 1\. Mục đích của tài liệu

Tài liệu xác định bối cảnh nghiệp vụ, mục tiêu sản phẩm, phạm vi chức năng, kiến trúc định hướng, yêu cầu bảo mật, tiêu chí chất lượng và lộ trình triển khai của nền tảng AI hỗ trợ dựng clip tuyên truyền.

Đây không phải tài liệu mô tả một bản trình diễn. Mọi quyết định thiết kế phải hướng đến khả năng vận hành ổn định, bảo trì lâu dài, kiểm soát dữ liệu, phân quyền người dùng, truy vết thay đổi và mở rộng cho nhiều đơn vị.

**Nguyên tắc dành cho agent:** Không tự ý giản lược sản phẩm thành một trang nhập nội dung rồi xuất video. Mọi triển khai phải bảo toàn quy trình nghiệp vụ, kiểm duyệt, bảo mật và lịch sử phiên bản.

# 2\. Bối cảnh hình thành sản phẩm

## 2.1. Bối cảnh nghiệp vụ

Ủy ban MTTQ Việt Nam cấp phường và các tổ chức thành viên thường xuyên tổ chức hội nghị, chương trình an sinh xã hội, trao quà, vận động quỹ, bảo vệ môi trường, tuyên truyền chính sách, hoạt động đoàn thể và phong trào tại khu dân cư. Hình ảnh và video của các hoạt động này thường được ghi bằng điện thoại rồi chia sẻ qua Zalo, Facebook hoặc lưu rải rác trên thiết bị cá nhân.

Việc sản xuất clip hiện phụ thuộc nhiều vào kỹ năng của từng cán bộ. Công đoạn chọn tư liệu, viết lời dẫn, tạo phụ đề, chèn logo, ghép nhạc và xuất nhiều tỷ lệ khung hình tốn thời gian; chất lượng giữa các đơn vị thiếu đồng nhất. Nội dung đôi khi chậm được đăng, khó truy xuất nguồn, khó xác định người đã duyệt và tiềm ẩn rủi ro về dữ liệu cá nhân, bản quyền hoặc sai số liệu.

## 2.2. Cơ hội ứng dụng AI

- Tóm tắt thông tin hoạt động từ phiếu nghiệp vụ, kế hoạch hoặc báo cáo.
- Đề xuất tiêu đề, thông điệp, kịch bản và lời dẫn bằng tiếng Việt.
- Sắp xếp ảnh/video theo cảnh và đề xuất thời lượng.
- Tạo giọng đọc tiếng Việt, phụ đề đồng bộ và nhạc nền phù hợp.
- Tự động dựng nhiều phiên bản clip theo bộ nhận diện được phê duyệt.
- Hỗ trợ kiểm tra lỗi chính tả, trường thông tin còn thiếu và dữ liệu nhạy cảm.

## 2.3. Nền tảng mã nguồn mở được lựa chọn

MoneyPrinterTurbo được lựa chọn làm nền tảng kỹ thuật ban đầu vì có giấy phép MIT, hỗ trợ kịch bản AI, tư liệu cục bộ, TTS, phụ đề, nhạc nền, nhiều tỷ lệ khung hình, API, CLI và Docker. Sản phẩm mới chỉ kế thừa các thành phần phù hợp; giao diện, nghiệp vụ, dữ liệu, bảo mật và quy trình kiểm duyệt sẽ được xây dựng lại.

**Quyết định kiến trúc:** MoneyPrinterTurbo là video engine tham chiếu và nền tảng tăng tốc, không phải toàn bộ sản phẩm cuối.

# 3\. Tuyên bố sản phẩm

**Tầm nhìn:** Xây dựng một nền tảng tiếng Việt an toàn, dễ sử dụng và có kiểm soát, giúp cơ quan và tổ chức tại cơ sở chuyển dữ liệu hoạt động thành clip tuyên truyền chất lượng trong thời gian ngắn.

## 3.1. Mục tiêu tổng quát

Hình thành quy trình số khép kín từ tiếp nhận tư liệu, tạo kịch bản, dựng bản nháp, kiểm tra, phê duyệt, xuất bản đến lưu trữ và đánh giá hiệu quả; trong đó AI đóng vai trò trợ lý, còn con người chịu trách nhiệm quyết định cuối cùng.

## 3.2. Mục tiêu cụ thể

- Giảm đáng kể thời gian sản xuất clip thông thường so với quy trình thủ công.
- Chuẩn hóa hình ảnh truyền thông giữa MTTQ phường và các tổ chức thành viên.
- Bảo đảm 100% clip chính thức trải qua bước kiểm tra và phê duyệt.
- Hỗ trợ tiếng Việt tốt ở kịch bản, giọng đọc, phụ đề và giao diện.
- Cho phép sử dụng ảnh/video thực tế của đơn vị thay vì chỉ lấy tư liệu Internet.
- Lưu được nguồn thông tin, phiên bản kịch bản, người thao tác và bản video đã duyệt.
- Có khả năng triển khai nội bộ hoặc trên hạ tầng được phép, không bắt buộc GPU.
- Mở rộng được sang nhiều phường, nhiều tổ chức và nhiều bộ nhận diện.

## 3.3. Những điều sản phẩm không hướng tới

- Không phải công cụ tạo video giải trí tự động để chạy theo xu hướng.
- Không tự động đăng clip khi chưa có người có thẩm quyền phê duyệt.
- Không thay thế trách nhiệm kiểm chứng thông tin của cán bộ.
- Không dùng AI để giả mạo phát biểu, khuôn mặt hoặc giọng nói của cá nhân.
- Không gửi dữ liệu nhạy cảm lên dịch vụ bên ngoài nếu chưa được cấu hình và cho phép.
- Không coi số lượt xem là thước đo duy nhất của hiệu quả tuyên truyền.

# 4\. Đối tượng sử dụng và vai trò

| **Vai trò**       | **Trách nhiệm chính**                                                                           |
| ----------------- | ----------------------------------------------------------------------------------------------- |
| Quản trị hệ thống | Cấu hình hệ thống, đơn vị, người dùng, quyền, nhà cung cấp AI, lưu trữ và chính sách bảo mật.   |
| Quản trị đơn vị   | Quản lý thành viên, bộ nhận diện, mẫu clip, kênh xuất bản và báo cáo của đơn vị.                |
| Cán bộ nội dung   | Tạo hồ sơ hoạt động, tải tư liệu, yêu cầu AI viết kịch bản, chỉnh sửa nội dung và tạo bản nháp. |
| Cán bộ kiểm tra   | Kiểm tra nguồn, số liệu, tên, chức danh, hình ảnh, phụ đề, bản quyền và dữ liệu cá nhân.        |
| Người phê duyệt   | Chấp thuận, yêu cầu chỉnh sửa hoặc từ chối phiên bản video trước khi xuất bản.                  |
| Cộng tác viên     | Tải tư liệu hoặc chuẩn bị bản nháp trong phạm vi được phân quyền; không được tự phê duyệt.      |
| Người xem báo cáo | Xem dashboard, hiệu quả truyền thông và lịch sử; không sửa dữ liệu.                             |

# 5\. Phạm vi nghiệp vụ đầu vào và đầu ra

## 5.1. Đầu vào

- Phiếu thông tin hoạt động có cấu trúc.
- Kế hoạch, báo cáo, bài tin hoặc nội dung văn bản đã được phép sử dụng.
- Ảnh JPG/PNG/WEBP và video MP4/MOV quay từ điện thoại hoặc máy ảnh.
- Logo, bộ nhận diện, mẫu mở đầu/kết thúc, nhạc và phông chữ hợp lệ.
- Kịch bản do người dùng tự nhập hoặc kịch bản do AI đề xuất.
- Bản ghi âm hoặc video phát biểu cần chuyển lời nói thành văn bản.

## 5.2. Đầu ra

- Clip dọc 9:16 cho nội dung ngắn trên thiết bị di động.
- Clip ngang 16:9 cho Facebook, website, màn hình và hội nghị.
- Tùy chọn clip vuông 1:1 nếu kênh phân phối yêu cầu.
- Kịch bản đã duyệt, lời dẫn, phụ đề SRT/VTT và tệp âm thanh.
- Ảnh bìa/thumbnail và nội dung mô tả đăng kèm.
- Hồ sơ phiên bản và báo cáo quá trình sản xuất.

# 6\. Quy trình nghiệp vụ bắt buộc

| **Bước** | **Trạng thái**           | **Mô tả bắt buộc**                                                                  |
| -------- | ------------------------ | ----------------------------------------------------------------------------------- |
| 1        | Tạo hồ sơ                | Nhập thông tin hoạt động, mục tiêu, đối tượng, kênh, thời lượng và người phụ trách. |
| 2        | Tải và chuẩn hóa tư liệu | Tải ảnh/video; kiểm tra định dạng, dung lượng, trùng lặp, chất lượng và metadata.   |
| 3        | Xử lý nội dung           | AI tóm tắt, đề xuất thông điệp và tạo kịch bản có dẫn chiếu nguồn.                  |
| 4        | Duyệt kịch bản           | Người dùng sửa; cán bộ kiểm tra xác nhận số liệu, tên và câu chữ trước khi dựng.    |
| 5        | Tạo storyboard           | Chia cảnh, ghép tư liệu, chọn bố cục, giọng đọc, phụ đề, nhạc và tỷ lệ.             |
| 6        | Render bản nháp          | Video worker dựng bản xem trước có watermark trạng thái nếu cần.                    |
| 7        | Kiểm tra video           | Rà soát hình ảnh, âm thanh, phụ đề, logo, bản quyền và dữ liệu cá nhân.             |
| 8        | Phê duyệt                | Người có thẩm quyền duyệt, từ chối hoặc yêu cầu sửa; ghi lý do và lịch sử.          |
| 9        | Render chính thức        | Xuất bản chất lượng cao từ đúng phiên bản đã duyệt.                                 |
| 10       | Phân phối và lưu trữ     | Tải xuống hoặc gửi đến kênh được cấu hình; lưu hồ sơ và số liệu hiệu quả.           |

**Kiểm soát quan trọng:** Trạng thái chưa duyệt không được xuất bản bằng kênh chính thức. Mọi lần render phải gắn với một phiên bản kịch bản và storyboard cụ thể.

# 7\. Danh mục chức năng đầy đủ

## 7.1. Quản lý tài khoản, đơn vị và phân quyền

- Đăng nhập, đăng xuất, đổi mật khẩu, khôi phục tài khoản và quản lý phiên đăng nhập.
- Hỗ trợ xác thực đa yếu tố cho vai trò quan trọng.
- Quản lý cơ cấu đơn vị: MTTQ phường, Đoàn Thanh niên, Hội LHPN, Hội Cựu chiến binh, Hội Nông dân và đơn vị mở rộng.
- Phân quyền theo vai trò và theo phạm vi dữ liệu của đơn vị.
- Tạm khóa người dùng, thu hồi phiên và ghi nhận hoạt động đăng nhập.
- Không cho một người tự tạo, tự kiểm tra và tự phê duyệt khi chính sách tách nhiệm vụ được bật.

## 7.2. Quản lý hồ sơ hoạt động

- Tạo, chỉnh sửa, lưu nháp, sao chép, lưu trữ và tìm kiếm hồ sơ hoạt động.
- Các trường: tên, loại hoạt động, thời gian, địa điểm, đơn vị, thành phần, mục tiêu, kết quả, số liệu, thông điệp, nguồn thông tin và người xác nhận.
- Gắn nhãn chủ đề: an sinh xã hội, môi trường, chính sách, phong trào, hội nghị, tuyên truyền pháp luật và nhóm tùy chỉnh.
- Đính kèm tài liệu nguồn; đánh dấu trường nào đã được xác nhận.
- Theo dõi trạng thái hồ sơ và lịch sử thay đổi.

## 7.3. Quản lý thư viện ảnh, video và âm thanh

- Tải một hoặc nhiều tệp bằng kéo-thả; tải lại khi lỗi; hiển thị tiến độ.
- Kiểm tra định dạng, dung lượng, codec, độ phân giải, tỷ lệ và khả năng giải mã.
- Tạo thumbnail, proxy video nhẹ và waveform âm thanh.
- Phát hiện tệp trùng bằng hash và cảnh báo chất lượng quá thấp.
- Gắn mô tả, người chụp, địa điểm, thời gian, quyền sử dụng và trạng thái đồng ý.
- Phân loại tư liệu theo hồ sơ, sự kiện, tổ chức, năm và nhãn.
- Xóa mềm, thời hạn lưu trữ và khôi phục theo quyền.

## 7.4. AI hỗ trợ tạo và kiểm tra nội dung

- Tạo tiêu đề, thông điệp, phần mở đầu, thân bài và lời kết bằng tiếng Việt.
- Tạo nhiều phiên bản theo thời lượng 30 giây, 60 giây, 90 giây hoặc tùy chỉnh.
- Chọn phong cách: trang trọng, gần gũi cộng đồng, bản tin, hướng dẫn hoặc tóm tắt sự kiện.
- Dẫn chiếu mỗi dữ kiện quan trọng đến trường dữ liệu hoặc tài liệu nguồn.
- Đánh dấu tên, ngày, số liệu và chức danh để người dùng xác nhận.
- Kiểm tra chính tả, câu quá dài, từ ngữ khó hiểu, nội dung lặp và trường còn thiếu.
- Không tự phát minh số liệu; nếu thiếu phải nêu rõ cần bổ sung.
- Lưu prompt, model, tham số và kết quả để truy vết, nhưng che khóa bí mật.
- Cho phép người dùng chỉnh sửa thủ công và so sánh các phiên bản.

## 7.5. Storyboard và trình biên tập cảnh

- Tự động chia kịch bản thành các cảnh có thời gian dự kiến.
- Đề xuất ảnh/video phù hợp từ thư viện nội bộ; không tự ý lấy tư liệu ngoài.
- Kéo-thả sắp xếp cảnh và tư liệu.
- Chọn kiểu hiển thị: toàn màn hình, ảnh trong khung, chia đôi, tiêu đề, con số nổi bật hoặc quote.
- Cắt đầu/cuối video, chọn đoạn sử dụng và điều chỉnh tốc độ trong giới hạn phù hợp.
- Hiệu ứng pan/zoom nhẹ cho ảnh, chuyển cảnh và thời gian hiển thị.
- Khóa cảnh đã được kiểm tra để tránh thay đổi ngoài ý muốn.
- Xem trước storyboard trước khi render.

## 7.6. Giọng đọc tiếng Việt và xử lý âm thanh

- Hỗ trợ nhiều nhà cung cấp TTS theo cơ chế adapter; cấu hình giọng nam/nữ, vùng giọng nếu dịch vụ hỗ trợ.
- Nghe thử từng đoạn trước khi tạo toàn bộ.
- Điều chỉnh tốc độ, cao độ, khoảng nghỉ và cách đọc từ viết tắt.
- Từ điển phát âm riêng cho tên người, địa danh và thuật ngữ.
- Trộn giọng đọc, âm thanh gốc và nhạc nền; tự giảm nhạc khi có lời.
- Chuẩn hóa âm lượng và cảnh báo âm thanh méo hoặc quá nhỏ.
- Cho phép dùng bản thu âm của cán bộ thay cho TTS.
- Không triển khai nhân bản giọng nói mặc định; chỉ xem xét sau khi có chính sách và sự đồng ý rõ ràng.

## 7.7. Phụ đề và chuyển lời nói thành văn bản

- Tạo phụ đề từ timestamp của TTS hoặc nhận diện lời nói bằng faster-whisper/giải pháp tương đương.
- Trình sửa phụ đề theo dòng thời gian.
- Tùy chỉnh phông, cỡ chữ, màu, nền, viền, vị trí và vùng an toàn.
- Kiểm tra số ký tự mỗi dòng, thời gian hiển thị và phụ đề chồng lấn.
- Xuất phụ đề nhúng trong video hoặc tệp SRT/VTT.
- Hỗ trợ phụ đề song ngữ Việt-Anh ở giai đoạn mở rộng.

## 7.8. Bộ nhận diện và mẫu clip

- Quản lý logo, màu sắc, phông chữ, intro, outro, lower-third, watermark và câu kết.
- Mỗi đơn vị có bộ nhận diện riêng nhưng kế thừa quy chuẩn chung.
- Mẫu clip theo loại hoạt động và tỷ lệ khung hình.
- Khóa các thành phần nhận diện bắt buộc để người dùng không vô tình xóa.
- Quản lý phiên bản mẫu; clip cũ tiếp tục tham chiếu đúng phiên bản đã dùng.

## 7.9. Nhạc nền và bản quyền tài nguyên

- Thư viện nhạc nội bộ có nguồn, giấy phép, phạm vi sử dụng và ngày hết hạn nếu có.
- Tìm kiếm theo tâm trạng, chủ đề và thời lượng.
- Cảnh báo khi nhạc hoặc tư liệu chưa có thông tin quyền sử dụng.
- Không mặc định sử dụng nhạc lấy ngẫu nhiên trên Internet.
- Lưu bằng chứng nguồn và giấy phép cùng hồ sơ clip.

## 7.10. Render và xử lý video

- Render preview độ phân giải thấp và render chính thức chất lượng cao.
- Hàng đợi tác vụ, ưu tiên, trạng thái, phần trăm tiến độ, hủy và chạy lại.
- Hỗ trợ 9:16, 16:9, 1:1; cấu hình 720p/1080p và bitrate.
- Không chặn giao diện trong thời gian render; gửi thông báo khi hoàn tất.
- Tự dọn tệp tạm theo chính sách và giữ log kỹ thuật cần thiết.
- Kiểm tra đầu ra: video phát được, có âm thanh, đúng thời lượng, không thiếu cảnh và không lỗi phụ đề.
- Gắn watermark 'BẢN NHÁP' cho phiên bản chưa duyệt nếu chính sách yêu cầu.

## 7.11. Kiểm tra, nhận xét và phê duyệt

- Gửi phiên bản đến người kiểm tra hoặc người phê duyệt.
- Đặt nhận xét theo mốc thời gian trên video.
- Checklist bắt buộc: tên, chức danh, ngày, địa điểm, số liệu, hình ảnh, phụ đề, logo, bản quyền và dữ liệu cá nhân.
- Hành động: chấp thuận, yêu cầu sửa, từ chối; bắt buộc ghi lý do khi trả lại hoặc từ chối.
- Không được sửa nội dung âm thầm sau khi duyệt; thay đổi phải tạo phiên bản mới.
- Lưu chữ ký nghiệp vụ dạng tài khoản, thời gian và dấu vết hệ thống.

## 7.12. Phân phối, xuất bản và chia sẻ

- Tải video, phụ đề, thumbnail và nội dung đăng kèm.
- Gửi sang kênh được tích hợp chỉ sau khi đã duyệt.
- Cấu hình lịch đăng, nhưng phải cho phép hủy trước thời điểm phát hành.
- Ghi nhận kênh, thời điểm, người thực hiện, URL hoặc mã bài đăng.
- Cơ chế retry an toàn, tránh đăng trùng khi API kênh lỗi.
- Giai đoạn đầu ưu tiên tải xuống thủ công; tích hợp trực tiếp Zalo OA/Facebook chỉ triển khai khi có API và quyền hợp lệ.

## 7.13. Kho lưu trữ, tìm kiếm và phiên bản

- Lưu hồ sơ, nguồn, kịch bản, storyboard, bản nháp, bản duyệt và bản xuất bản.
- Tìm theo từ khóa, thời gian, đơn vị, loại hoạt động, trạng thái và người phụ trách.
- So sánh phiên bản kịch bản và lịch sử thao tác.
- Chính sách lưu trữ theo loại dữ liệu; xóa mềm và quy trình xóa vĩnh viễn có thẩm quyền.
- Xuất hồ sơ kiểm toán khi cần.

## 7.14. Dashboard và đo lường

- Số hồ sơ, số clip, thời gian xử lý trung bình và tỷ lệ hoàn thành đúng hạn.
- Số vòng chỉnh sửa, lỗi thường gặp và tỷ lệ được duyệt lần đầu.
- Chi phí AI/TTS/render theo đơn vị và theo kỳ.
- Số liệu kênh: lượt xem, tỷ lệ xem hết, tương tác và phản hồi nếu kênh cung cấp API.
- Báo cáo không dùng chỉ số tương tác làm căn cứ duy nhất; phải gắn với mục tiêu truyền thông.
- Xuất báo cáo CSV/XLSX/PDF ở giai đoạn phù hợp.

## 7.15. Cấu hình AI và quản trị chi phí

- Adapter cho LLM, TTS, STT và nguồn tư liệu; không khóa cứng vào một nhà cung cấp.
- Cho phép dùng mô hình nội bộ hoặc API bên ngoài theo chính sách.
- Quản lý khóa bí mật bằng biến môi trường hoặc secret manager, không lưu thẳng trong mã nguồn.
- Hạn mức theo người dùng/đơn vị, ước tính chi phí trước tác vụ và cảnh báo vượt ngân sách.
- Fallback khi nhà cung cấp lỗi và cơ chế circuit breaker.
- Mọi thay đổi cấu hình quan trọng phải được ghi audit log.

## 7.16. Quản trị hệ thống và vận hành

- Dashboard sức khỏe dịch vụ, worker, hàng đợi, lưu trữ và lỗi render.
- Log có cấu trúc, correlation ID và cảnh báo lỗi.
- Sao lưu, khôi phục, kiểm tra bản sao lưu và kế hoạch phục hồi sự cố.
- Quản lý phiên bản phần mềm, migration dữ liệu và rollback.
- Trang trạng thái và hướng dẫn xử lý lỗi phổ biến cho quản trị viên.

# 8\. Chức năng AI nâng cao theo lộ trình

| **Chức năng**              | **Phạm vi định hướng**                                                                          |
| -------------------------- | ----------------------------------------------------------------------------------------------- |
| Rút gọn video dài          | Tự phát hiện khoảng im lặng, đoạn lặp và gợi ý phần nổi bật; người dùng xác nhận trước khi cắt. |
| Tóm tắt hội nghị           | Chuyển lời nói thành văn bản, phân đoạn người nói, trích ý chính và tạo clip 1-3 phút.          |
| Bài tin từ clip            | Tạo bài viết, mô tả và tiêu đề từ nội dung đã duyệt của video.                                  |
| Bảo vệ riêng tư            | Phát hiện khuôn mặt, biển số, số điện thoại hoặc giấy tờ để gợi ý làm mờ; con người xác nhận.   |
| Đánh giá khả năng tiếp cận | Kiểm tra cỡ chữ, độ tương phản, tốc độ phụ đề và mức âm lượng.                                  |
| Phụ đề song ngữ            | Tạo bản Việt-Anh có quy trình kiểm tra riêng.                                                   |
| Gợi ý tái sử dụng          | Đề xuất tư liệu cũ phù hợp theo chủ đề nhưng phải tôn trọng quyền sử dụng và bối cảnh.          |

# 9\. Yêu cầu dữ liệu và bảo vệ thông tin

## 9.1. Phân loại dữ liệu

| **Loại**         | **Ví dụ**                                              | **Nguyên tắc**                                                 |
| ---------------- | ------------------------------------------------------ | -------------------------------------------------------------- |
| Công khai        | Clip đã phát hành, thông tin sự kiện đã công bố.       | Có thể phân phối theo quyền.                                   |
| Nội bộ           | Bản nháp, kế hoạch, nhận xét, log nghiệp vụ.           | Chỉ người được phân quyền.                                     |
| Dữ liệu cá nhân  | Họ tên, hình ảnh, giọng nói, liên hệ, địa chỉ.         | Có mục đích, căn cứ và kiểm soát truy cập.                     |
| Nhạy cảm/hạn chế | CCCD, hoàn cảnh cá nhân, hồ sơ phản ánh, tài liệu mật. | Không đưa vào luồng AI thông thường; áp dụng chính sách riêng. |

## 9.2. Nguyên tắc bắt buộc

- Thu thập tối thiểu đúng mục đích; không yêu cầu dữ liệu không cần thiết.
- Mã hóa khi truyền và khi lưu đối với dữ liệu cần bảo vệ.
- Tách kho tệp, database và secret; cấp quyền theo nguyên tắc tối thiểu.
- Không ghi API key, token hoặc dữ liệu nhạy cảm vào log.
- Có thời hạn lưu trữ, quy trình xóa và khả năng đáp ứng yêu cầu truy xuất.
- Cấu hình rõ dữ liệu nào được phép gửi đến nhà cung cấp AI bên ngoài.
- Mọi tính năng nhận diện hoặc làm mờ tự động chỉ là gợi ý, không thay thế kiểm tra con người.
- Thực hiện rà soát pháp lý và an toàn thông tin trước khi đưa vào vận hành chính thức.

# 10\. Yêu cầu phi chức năng

| **Thuộc tính**    | **Yêu cầu**                                                                                     |
| ----------------- | ----------------------------------------------------------------------------------------------- |
| Khả dụng          | Thiết kế hướng đến vận hành liên tục; lỗi một tác vụ render không làm sập toàn hệ thống.        |
| Hiệu năng         | Các trang nghiệp vụ thông thường phản hồi nhanh; tác vụ nặng chạy bất đồng bộ và có tiến độ.    |
| Mở rộng           | Có thể tăng số worker render độc lập khi số lượng người dùng tăng.                              |
| Tương thích       | Giao diện responsive, ưu tiên Chrome/Edge hiện hành; backend triển khai Linux/Docker.           |
| Khả năng tiếp cận | Phím tắt cơ bản, nhãn trường rõ, tương phản phù hợp, phụ đề và thông báo lỗi dễ hiểu.           |
| Bảo trì           | Module hóa, API có phiên bản, migration dữ liệu, kiểm thử và tài liệu kỹ thuật.                 |
| Quan sát hệ thống | Metrics, log, trace và cảnh báo; mỗi job có correlation ID.                                     |
| Khôi phục         | Sao lưu tự động, kiểm thử restore và quy định RPO/RTO trước production.                         |
| Chất lượng video  | Đầu ra đúng codec, độ phân giải, tỷ lệ, âm lượng, không mất cảnh hoặc phụ đề.                   |
| Bản địa hóa       | Toàn bộ giao diện, lỗi, trợ giúp và mẫu nội dung bằng tiếng Việt; cấu trúc sẵn cho đa ngôn ngữ. |

# 11\. Kiến trúc kỹ thuật định hướng

Kiến trúc đề xuất tách nghiệp vụ khỏi xử lý video để tránh phụ thuộc chặt vào MoneyPrinterTurbo và thuận lợi mở rộng.

| **Thành phần**  | **Công nghệ định hướng** | **Trách nhiệm**                                                              |
| --------------- | ------------------------ | ---------------------------------------------------------------------------- |
| Web Frontend    | React + TypeScript       | Giao diện nghiệp vụ, upload, chỉnh kịch bản, storyboard, preview, phê duyệt. |
| Core Backend    | Spring Boot              | Tài khoản, phân quyền, hồ sơ, workflow, audit, báo cáo, API.                 |
| Video/AI Worker | Python                   | Adapter MoneyPrinterTurbo, TTS, STT, MoviePy/FFmpeg, render.                 |
| Database        | PostgreSQL ưu tiên       | Dữ liệu nghiệp vụ, trạng thái, phiên bản và audit metadata.                  |
| Object Storage  | MinIO/S3-compatible      | Ảnh, video, âm thanh, phụ đề, thumbnail và đầu ra.                           |
| Queue/Cache     | Redis + queue phù hợp    | Job bất đồng bộ, tiến độ, retry, lock và cache.                              |
| Media Engine    | FFmpeg/MoviePy           | Chuẩn hóa, cắt ghép, trộn âm thanh và xuất video.                            |
| AI Providers    | Adapter                  | LLM, TTS, STT cục bộ hoặc bên ngoài theo cấu hình.                           |

## 11.1. Nguyên tắc tích hợp MoneyPrinterTurbo

- Fork một phiên bản được kiểm soát; ghi lại commit/tag nguồn và giấy phép.
- Không để frontend gọi trực tiếp MoneyPrinterTurbo.
- Bọc chức năng cần dùng qua Video Worker/API nội bộ.
- Tách adapter TTS, LLM, nguồn tư liệu và renderer để có thể thay thế.
- Không sửa sâu upstream nếu có thể mở rộng bằng adapter hoặc module riêng.
- Theo dõi cập nhật bảo mật; thử nghiệm nâng cấp trên môi trường staging.
- Giữ thông báo bản quyền và danh sách thành phần mã nguồn mở.

## 11.2. Ranh giới kế thừa và phát triển mới

| **Nhóm**          | **Phạm vi**                                                                                                                                 |
| ----------------- | ------------------------------------------------------------------------------------------------------------------------------------------- |
| Kế thừa/tham khảo | Tạo kịch bản, TTS adapter, phụ đề, xử lý nhạc, phối ghép cơ bản, cấu hình tỷ lệ, FFmpeg/MoviePy, Docker.                                    |
| Viết mới          | React UI, Spring Boot backend, RBAC, workflow duyệt, hồ sơ hoạt động, audit, phiên bản, thư viện tư liệu, dashboard, quản lý quyền sử dụng. |
| Cải tiến          | Hỗ trợ tiếng Việt, storyboard, hàng đợi, preview, xử lý lỗi, quản trị chi phí, lưu trữ S3 và bảo mật production.                            |
| Chưa đưa vào lõi  | Tự động đăng đa nền tảng, tạo avatar, nhân bản giọng, sinh hình ảnh hoàn toàn tự động.                                                      |

# 12\. Mô hình dữ liệu nghiệp vụ cấp cao

| **Thực thể**             | **Ý nghĩa**                                       |
| ------------------------ | ------------------------------------------------- |
| Organization             | Đơn vị và cấu trúc tổ chức.                       |
| User / Role / Permission | Tài khoản và phân quyền.                          |
| Activity                 | Hồ sơ hoạt động và dữ kiện nguồn.                 |
| Asset                    | Ảnh, video, âm thanh, tài liệu và quyền sử dụng.  |
| Script / ScriptVersion   | Kịch bản cùng lịch sử phiên bản và nguồn dữ kiện. |
| Storyboard / Scene       | Bố cục cảnh, thời gian, tư liệu và lớp hiển thị.  |
| Voiceover / Subtitle     | Âm thanh lời đọc và phụ đề.                       |
| BrandKit / Template      | Bộ nhận diện và mẫu clip có phiên bản.            |
| RenderJob / VideoVersion | Tác vụ render và đầu ra.                          |
| Review / Approval        | Nhận xét, checklist và quyết định duyệt.          |
| Publication              | Kênh, thời gian và kết quả phát hành.             |
| AuditLog                 | Dấu vết thao tác không chỉnh sửa tùy tiện.        |

# 13\. Trạng thái chính của sản phẩm

| **Trạng thái**     | **Ý nghĩa**                          |
| ------------------ | ------------------------------------ |
| DRAFT              | Hồ sơ đang soạn.                     |
| READY_FOR_SCRIPT   | Đủ dữ liệu để tạo kịch bản.          |
| SCRIPT_REVIEW      | Kịch bản đang kiểm tra.              |
| SCRIPT_APPROVED    | Kịch bản đã được duyệt để dựng.      |
| STORYBOARD_EDITING | Đang sắp xếp cảnh.                   |
| RENDERING_PREVIEW  | Đang dựng bản nháp.                  |
| VIDEO_REVIEW       | Video đang kiểm tra.                 |
| CHANGES_REQUESTED  | Có yêu cầu chỉnh sửa.                |
| APPROVED           | Phiên bản được phê duyệt.            |
| RENDERING_FINAL    | Đang xuất bản chính thức.            |
| READY_TO_PUBLISH   | Sẵn sàng phân phối.                  |
| PUBLISHED          | Đã xuất bản.                         |
| ARCHIVED           | Đã lưu trữ.                          |
| FAILED             | Tác vụ thất bại và có thể xử lý lại. |

# 14\. Tiêu chí nghiệm thu cấp sản phẩm

- Một người dùng được phân quyền có thể hoàn tất quy trình từ hồ sơ đến video chính thức mà không thao tác bằng dòng lệnh.
- Sản phẩm sử dụng tốt với nội dung tiếng Việt và không làm mất dấu tiếng Việt.
- Có thể dùng hoàn toàn tư liệu nội bộ của đơn vị.
- Mọi clip chính thức truy ngược được hồ sơ, kịch bản, storyboard, người duyệt và phiên bản mẫu.
- Hệ thống ngăn xuất bản phiên bản chưa được phê duyệt.
- Job render lỗi không làm mất hồ sơ; người dùng thấy nguyên nhân dễ hiểu và có thể thử lại.
- Quyền truy cập được kiểm thử; người dùng không xem được dữ liệu ngoài phạm vi.
- API key và secret không xuất hiện trong repository, response hoặc log.
- Có sao lưu, hướng dẫn khôi phục và giám sát production.
- Có tài liệu triển khai, vận hành, cập nhật và xử lý sự cố.
- Có kiểm thử tự động cho nghiệp vụ trọng yếu và kiểm thử đầu-cuối cho luồng chính.
- Có danh mục giấy phép mã nguồn mở và tài nguyên media.

# 15\. Phạm vi MVP vận hành thật

MVP không đồng nghĩa với demo. MVP là phiên bản nhỏ nhất có thể đưa vào sử dụng có kiểm soát tại một đơn vị thí điểm, với đầy đủ bảo mật, phân quyền, sao lưu và quy trình duyệt tối thiểu.

| **Ưu tiên** | **Hạng mục**                                                 |
| ----------- | ------------------------------------------------------------ |
| P0          | Đăng nhập, RBAC, quản lý đơn vị và audit cơ bản.             |
| P0          | Hồ sơ hoạt động và thư viện tư liệu cục bộ.                  |
| P0          | Tạo/chỉnh/duyệt kịch bản tiếng Việt.                         |
| P0          | Storyboard cơ bản từ ảnh/video nội bộ.                       |
| P0          | TTS tiếng Việt, phụ đề, nhạc và bộ nhận diện.                |
| P0          | Render preview/final 9:16 và 16:9 bằng hàng đợi.             |
| P0          | Kiểm tra, nhận xét, yêu cầu sửa và phê duyệt.                |
| P0          | Lưu phiên bản, tải đầu ra và lịch sử.                        |
| P0          | Quản lý secret, sao lưu, log, giám sát và tài liệu vận hành. |
| P1          | Nhận diện lời nói trong video và chỉnh phụ đề.               |
| P1          | Dashboard hiệu suất và chi phí.                              |
| P1          | Lịch đăng và tích hợp kênh nếu API hợp lệ.                   |
| P2          | Rút gọn hội nghị, làm mờ dữ liệu và phụ đề song ngữ.         |

# 16\. Lộ trình triển khai đề xuất

| **Giai đoạn**                  | **Thời lượng tham khảo** | **Kết quả**                                                               |
| ------------------------------ | ------------------------ | ------------------------------------------------------------------------- |
| Giai đoạn 0 - Khảo sát         | 2-3 tuần                 | Chốt nghiệp vụ, chính sách dữ liệu, hạ tầng, bộ mẫu và tiêu chí thí điểm. |
| Giai đoạn 1 - Nền tảng         | 4-6 tuần                 | Kiến trúc, CI/CD, tài khoản, RBAC, hồ sơ, lưu trữ, queue, audit.          |
| Giai đoạn 2 - Video MVP        | 6-10 tuần                | Tích hợp engine, kịch bản, storyboard, TTS, phụ đề, template, render.     |
| Giai đoạn 3 - Duyệt & vận hành | 4-6 tuần                 | Workflow, checklist, versioning, giám sát, backup, hardening.             |
| Giai đoạn 4 - Thí điểm         | 6-8 tuần                 | Chạy thật tại một đơn vị, đào tạo, đo lường, sửa lỗi và hoàn thiện SOP.   |
| Giai đoạn 5 - Mở rộng          | Theo kết quả             | Đa đơn vị, dashboard, kênh xuất bản, AI nâng cao và tối ưu chi phí.       |

# 17\. Chỉ số đánh giá khi vận hành

| **Nhóm chỉ số**    | **Nội dung**                                                                   |
| ------------------ | ------------------------------------------------------------------------------ |
| Thời gian sản xuất | Từ lúc đủ tư liệu đến bản nháp đầu tiên; từ lúc tạo hồ sơ đến clip được duyệt. |
| Chất lượng         | Số lỗi tên, số liệu, phụ đề, nhận diện và bản quyền trên mỗi clip.             |
| Quy trình          | Tỷ lệ clip đúng luồng; tỷ lệ duyệt lần đầu; số vòng chỉnh sửa.                 |
| Sử dụng            | Người dùng hoạt động, hồ sơ/clip theo đơn vị, tỷ lệ hoàn thành.                |
| Kỹ thuật           | Tỷ lệ render thành công, thời gian queue, thời gian render, dung lượng.        |
| Chi phí            | Chi phí LLM/TTS/STT/lưu trữ/render theo clip và theo đơn vị.                   |
| Truyền thông       | Lượt xem, tỷ lệ xem hết, tương tác và phản hồi theo mục tiêu.                  |
| An toàn            | Sự cố quyền truy cập, dữ liệu cá nhân, bản quyền và số lần cảnh báo.           |

# 18\. Rủi ro và biện pháp kiểm soát

| **Rủi ro**             | **Biện pháp**                                                    | **Mức**    |
| ---------------------- | ---------------------------------------------------------------- | ---------- |
| AI tạo sai dữ kiện     | Dẫn chiếu nguồn, trường xác nhận, duyệt kịch bản bắt buộc.       | Cao        |
| Lộ dữ liệu cá nhân     | Phân loại, kiểm soát provider, mã hóa, RBAC, log và đào tạo.     | Cao        |
| Vi phạm bản quyền      | Thư viện có metadata giấy phép; chặn tài nguyên không rõ nguồn.  | Cao        |
| Phụ thuộc upstream     | Fork có kiểm soát, adapter, pin phiên bản, test nâng cấp.        | Trung bình |
| Render chậm hoặc lỗi   | Queue, retry, worker độc lập, proxy, giám sát và quota.          | Trung bình |
| Chi phí API tăng       | Hạn mức, ước tính chi phí, cache, provider thay thế.             | Trung bình |
| Người dùng khó sử dụng | Giao diện tiếng Việt, template, hướng dẫn theo bước và tập huấn. | Trung bình |
| Đăng nhầm bản nháp     | Watermark, trạng thái, quyền xuất bản và idempotency.            | Cao        |
| Thay đổi sau phê duyệt | Phiên bản bất biến; sửa đổi tạo bản mới và duyệt lại.            | Cao        |

# 19\. Quy tắc thực thi dành cho coding agent

- Đọc tài liệu này trước khi lập kế hoạch; không tự thay đổi phạm vi P0 nếu chưa có quyết định.
- Trước mỗi module, viết rõ user story, acceptance criteria, API contract và migration.
- Không đưa secret, dữ liệu thật hoặc file media nhạy cảm vào repository/test fixture.
- Ưu tiên kiến trúc module hóa; MoneyPrinterTurbo chỉ được truy cập qua lớp adapter/service.
- Mọi endpoint phải có xác thực, ủy quyền, validation, giới hạn kích thước và xử lý lỗi.
- Job render phải idempotent ở mức phù hợp, có trạng thái, retry có kiểm soát và cleanup.
- Mọi thay đổi trạng thái phê duyệt phải nằm trong transaction và ghi audit.
- Viết unit test, integration test và end-to-end test cho luồng P0.
- Không tuyên bố hoàn thành nếu chưa kiểm thử, migration chưa chạy, hoặc chưa có hướng dẫn vận hành.
- Mỗi quyết định kỹ thuật khác tài liệu phải ghi ADR và nêu tác động.
- Phải kiểm tra giấy phép các dependency mới trước khi thêm.
- Không triển khai chức năng tự động đăng hoặc nhân bản giọng khi chưa có phê duyệt phạm vi.

# 20\. Cấu trúc tài liệu kỹ thuật agent phải tạo tiếp

| **Tệp**                     | **Nội dung bắt buộc**                             |
| --------------------------- | ------------------------------------------------- |
| 01-product-requirements.md  | Chi tiết user story và acceptance criteria.       |
| 02-system-architecture.md   | Kiến trúc, sơ đồ, ranh giới service và ADR.       |
| 03-data-model.md            | ERD, schema, index, retention và migration.       |
| 04-api-contract.yaml        | OpenAPI cho frontend, backend và video worker.    |
| 05-security-threat-model.md | Tài sản, threat, biện pháp và kiểm thử.           |
| 06-media-pipeline.md        | Codec, chuẩn hóa, render, retry, cleanup và QA.   |
| 07-deployment-runbook.md    | Triển khai, backup, restore, monitor và incident. |
| 08-test-strategy.md         | Các tầng kiểm thử, fixture và tiêu chí release.   |
| 09-open-source-notices.md   | Nguồn, phiên bản và giấy phép dependency.         |
| 10-pilot-plan.md            | Đơn vị thí điểm, đào tạo, hỗ trợ và chỉ số.       |

# 21\. Nguồn tham chiếu chính

| **Nguồn**         | **Liên kết**                                     | **Mục đích**                              |
| ----------------- | ------------------------------------------------ | ----------------------------------------- |
| MoneyPrinterTurbo | <https://github.com/harry0703/MoneyPrinterTurbo> | Nền tảng video mã nguồn mở được lựa chọn. |
| MoviePy           | <https://github.com/Zulko/moviepy>               | Thư viện xử lý và dựng video Python.      |
| FFmpeg            | <https://ffmpeg.org/>                            | Nền tảng xử lý media.                     |
| faster-whisper    | <https://github.com/SYSTRAN/faster-whisper>      | Nhận diện lời nói và phụ đề.              |

_Khi triển khai, nhóm dự án phải rà soát phiên bản, giấy phép và điều khoản của từng dịch vụ AI/TTS/STT cụ thể tại thời điểm sử dụng._

# 22\. Kết luận định hướng

Sản phẩm được xây dựng để giải quyết một quy trình nghiệp vụ thực tế, không phải để minh họa khả năng tạo video bằng AI. Giá trị cốt lõi không chỉ nằm ở việc render clip nhanh, mà ở khả năng bảo đảm nội dung đúng, tư liệu có nguồn, dữ liệu được bảo vệ, người có trách nhiệm phê duyệt và toàn bộ quá trình có thể truy vết.

MoneyPrinterTurbo giúp rút ngắn phần phát triển media pipeline, nhưng sản phẩm cuối phải có kiến trúc, giao diện và quy trình riêng phù hợp với người Việt Nam và hoạt động tuyên truyền tại cơ sở.

**Kết quả mong muốn:** Một nền tảng tiếng Việt có thể triển khai, vận hành, giám sát và mở rộng trong thực tế; tạo clip nhanh nhưng vẫn giữ con người ở vị trí kiểm soát cuối cùng.