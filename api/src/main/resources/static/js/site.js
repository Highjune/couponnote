(function () {
  var toast = document.getElementById('toast');
  var timer;
  function show(msg) {
    if (!toast) return;
    toast.textContent = msg;
    toast.classList.add('on');
    clearTimeout(timer);
    timer = setTimeout(function () { toast.classList.remove('on'); }, 2200);
  }
  function copy(code) {
    if (!code) return Promise.resolve(false);
    if (navigator.clipboard && window.isSecureContext) {
      return navigator.clipboard.writeText(code).then(function () { return true; }, function () { return false; });
    }
    var ta = document.createElement('textarea');
    ta.value = code; ta.setAttribute('readonly', ''); ta.style.position = 'fixed'; ta.style.top = '-1000px';
    document.body.appendChild(ta); ta.select();
    var ok = false; try { ok = document.execCommand('copy'); } catch (e) {}
    document.body.removeChild(ta);
    return Promise.resolve(ok);
  }
  document.addEventListener('click', function (e) {
    var el = e.target.closest('[data-code]');
    if (!el) return;
    var code = el.getAttribute('data-code');
    if (!code) return;
    // 버튼(복사)은 복사만, 링크(받으러 가기)는 복사 후 기본 동작(새 탭)을 그대로 둔다
    copy(code).then(function (ok) {
      show(ok ? '코드 ' + code + ' 복사됨 · 결제창에 붙여넣으세요' : '복사 실패 · 코드를 직접 입력하세요: ' + code);
    });
  });
})();
