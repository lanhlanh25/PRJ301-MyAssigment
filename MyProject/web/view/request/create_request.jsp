<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <title>Tạo Đơn Xin Nghỉ Phép</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <style>
    /* ========= Pastel Modern System ========= */
    :root{
      --bg-1:#f7f6ff; --bg-2:#fef9f5;
      --card:#ffffff; --ink:#262a35; --muted:#6b7082;
      --line:#e8eaf3; --line-2:#dfe3f2;
      --brand:#9e8cff; --brand-2:#89c5ff; /* Lavender + Sky */
      --ok:#8fd8c7; --warn:#ffcfac;
      --focus:rgba(158,140,255,.26);
      --radius:16px; --radius-sm:12px;
      --shadow:0 10px 28px rgba(35,38,70,.08);
      --shadow-sm:0 4px 14px rgba(25,27,45,.06);
    }

    *{box-sizing:border-box;margin:0;padding:0}
    html,body{height:100%}
    body{
      font-family:"Segoe UI",system-ui,-apple-system,Arial,sans-serif;
      color:var(--ink);
      background:
        radial-gradient(900px 420px at 0% -10%, var(--bg-1) 0%, transparent 60%),
        radial-gradient(900px 420px at 100% 0%, var(--bg-2) 0%, transparent 60%),
        linear-gradient(120deg, var(--bg-1), var(--bg-2));
      display:flex;align-items:center;justify-content:center;
      padding:28px;
    }

    .shell{
      width:100%;max-width:760px;
      background:var(--card);
      border:1px solid var(--line);
      border-radius:calc(var(--radius) + 4px);
      box-shadow:var(--shadow);
      overflow:hidden;
      animation:fade .35s ease;
    }

    .topbar{
      height:6px;
      background:linear-gradient(90deg, var(--brand), var(--brand-2));
    }

    .container{padding:26px 26px 22px}

    header{
      display:flex;align-items:center;justify-content:space-between;gap:14px;margin-bottom:16px;
    }
    .title{
      display:flex;align-items:center;gap:12px;
    }
    .logo-badge{
      width:40px;height:40px;border-radius:12px;
      background:linear-gradient(135deg, rgba(158,140,255,.18), rgba(137,197,255,.18));
      display:grid;place-items:center;
      border:1px solid var(--line);
      box-shadow:var(--shadow-sm);
      font-size:18px;
    }
    h1{font-size:20px;letter-spacing:.2px}
    .chip{
      font-size:12.5px;padding:6px 10px;border-radius:999px;color:#2e3142;
      background:linear-gradient(90deg, rgba(158,140,255,.15), rgba(137,197,255,.15));
      border:1px solid var(--line);
      white-space:nowrap;
    }

    .identity{
      display:flex;flex-wrap:wrap;gap:10px;
      background:linear-gradient(180deg,#fafaff,#fff);
      border:1px solid var(--line);border-radius:12px;padding:12px 14px;margin-bottom:16px;
      color:#4f556b;font-size:13.5px;
    }
    .identity b{color:#33384e}

    form{display:flex;flex-direction:column;gap:14px}

    .row{display:grid;grid-template-columns:1fr 1fr;gap:12px}
    .row-3{display:grid;grid-template-columns:1fr 1fr 1fr;gap:12px}
    @media (max-width:680px){
      .row,.row-3{grid-template-columns:1fr}
      .container{padding:22px 18px}
    }

    label{
      font-size:13.5px;color:#424760;font-weight:700;margin-bottom:6px;display:block;
    }
    select, input[type="date"], textarea, input[type="text"]{
      width:100%; border:1px solid var(--line); border-radius:var(--radius-sm);
      background:#fff; padding:12px 12px; font-size:14px; color:var(--ink);
      transition:border-color .2s, box-shadow .2s, transform .06s, background .2s;
    }
    select:hover, input[type="date"]:hover, textarea:hover, input[type="text"]:hover{border-color:#d6daf2}
    select:focus, input[type="date"]:focus, textarea:focus, input[type="text"]:focus{
      outline:none;border-color:var(--brand);box-shadow:0 0 0 5px var(--focus);
    }

    textarea{min-height:120px;resize:vertical}
    .hint{font-size:12.5px;color:var(--muted);margin-top:-4px}

    .inline{
      display:flex;align-items:center;gap:10px;flex-wrap:wrap;color:#58608a;font-size:13px
    }

    .divider{
      height:1px;background:var(--line-2);margin:6px 0 4px;
    }

    .actions{display:flex;gap:10px;align-items:center;margin-top:6px}
    .btn{
      appearance:none;border:0;border-radius:12px;padding:12px 16px;font-weight:800;font-size:14px;
      cursor:pointer;display:inline-flex;align-items:center;justify-content:center;gap:8px;
      transition:transform .06s, filter .2s, background .2s;
    }
    .btn:active{transform:translateY(1px)}
    .btn-primary{
      color:#fff;background:linear-gradient(90deg, var(--brand), var(--brand-2));
    }
    .btn-primary:hover{filter:brightness(1.03)}
    .btn-ghost{
      background:linear-gradient(0deg,#f6f7ff,#fff);
      border:1px solid var(--line);color:#303651;
    }
    .btn-ghost:hover{filter:brightness(0.98)}
    .btn[disabled]{opacity:.6;cursor:not-allowed}

    .foot-note{margin-top:8px;color:var(--muted);font-size:12.5px;text-align:center}

    @keyframes fade{from{opacity:0;transform:translateY(8px)}to{opacity:1;transform:none}}
  </style>
</head>
<body>
  <div class="shell">
    <div class="topbar"></div>
    <div class="container">
      <header>
        <div class="title">
          <div class="logo-badge">🌿</div>
          <div>
            <h1>Đơn xin nghỉ phép</h1>
            <div class="inline">
              <span>Điền thông tin rõ ràng, thời gian hợp lý.</span>
            </div>
          </div>
        </div>
        <span class="chip">Pastel • Modern</span>
      </header>

      <%
        model.iam.User user = (model.iam.User) session.getAttribute("auth");
        String roleName = (user != null && !user.getRoles().isEmpty())
                ? user.getRoles().get(0).getName() : "N/A";
        String display = (user != null) ? user.getDisplayname() : "Người dùng";
        String deptName = (user != null && user.getEmployee()!=null && user.getEmployee().getDept()!=null)
                ? ((model.Division)user.getEmployee().getDept()).getName() : "Phòng ban";
      %>

      <div class="identity">
        <div><b><%= display %></b></div>
        <div>• <%= deptName %></div>
        <div>• Vai trò: <b><%= roleName %></b></div>
      </div>

      <form action="create" method="POST" id="leaveForm" novalidate>
        <div class="row-3">
          <div>
            <label for="type">Loại đơn</label>
            <select id="type" name="type" required>
              <option value="" hidden>— Chọn loại đơn —</option>
              <option value="Nghỉ phép năm">Nghỉ phép năm</option>
              <option value="Nghỉ ốm">Nghỉ ốm</option>
              <option value="Nghỉ việc riêng">Nghỉ việc riêng</option>
              <option value="Nghỉ không lương">Nghỉ không lương</option>
            </select>
            <div class="hint">Sẽ hiển thị ở tiêu đề đơn</div>
          </div>
          <div>
            <label for="fromDate">Từ ngày</label>
            <input type="date" id="fromDate" name="fromDate" required />
            <div class="hint">Ngày bắt đầu nghỉ</div>
          </div>
          <div>
            <label for="toDate">Tới ngày</label>
            <input type="date" id="toDate" name="toDate" required />
            <div class="hint">Ngày kết thúc dự kiến</div>
          </div>
        </div>

        <div>
          <label for="reason">Lý do</label>
          <textarea id="reason" name="reason" maxlength="500"
            placeholder="Ví dụ: Khám sức khỏe, việc gia đình..." required></textarea>
          <div class="inline">
            <span class="hint">Trình bày ngắn gọn, tối đa 500 ký tự.</span>
            <span id="count" class="hint">0/500</span>
          </div>
        </div>

        <div class="divider"></div>

        <div class="actions">
          <button type="submit" class="btn btn-primary" id="submitBtn">📩 Gửi đơn</button>
          <a href="${pageContext.request.contextPath}/home" class="btn btn-ghost" role="button">Hủy</a>
        </div>
      </form>

      <div class="foot-note">
        Lưu ý: <b>Chỉ gửi 1 lần</b>. Bạn có thể sửa/hủy trước khi cấp trên duyệt (nếu hệ thống cho phép).
      </div>
    </div>
  </div>

  <script>
    // Đếm ký tự lý do
    (function(){
      const reason = document.getElementById('reason');
      const count  = document.getElementById('count');
      const submit = document.getElementById('submitBtn');
      const form   = document.getElementById('leaveForm');

      const updateCount = () => {
        count.textContent = (reason.value.length) + "/500";
      };
      reason.addEventListener('input', updateCount);
      updateCount();

      // Chặn gửi trùng, validate đơn giản khoảng ngày
      form.addEventListener('submit', function(e){
        const f = document.getElementById('fromDate').value;
        const t = document.getElementById('toDate').value;
        if(f && t && new Date(f) > new Date(t)){
          e.preventDefault();
          alert("Khoảng thời gian không hợp lệ: 'Từ ngày' phải trước hoặc bằng 'Tới ngày'.");
          return false;
        }
        submit.setAttribute('disabled', 'disabled');
      });
    })();
  </script>
</body>
</html>
