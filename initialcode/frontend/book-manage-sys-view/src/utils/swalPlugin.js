// swalPlugin.js
import Swal from 'sweetalert2';
// 五種圖標： success\error\info\warning\question
// 提示框：
// this.$swal.fire({
//     title: '退出登入',
//     text: '您已成功退出登入。',
//     icon: 'success', // 使用'success'圖標表示操作成功
//     showConfirmButton: false, // 隱藏確認按鈕，使得彈窗只展示資訊後自動關閉
//     timer: 2000, // 自動關閉彈窗的延遲時間，這裡是2秒
//   });

const swalPlugin = {
  install(Vue) {
    Vue.prototype.$swalConfirm = async function(options = {}) {
      const defaultOptions = {
        title: '提示',
        text: '',
        icon: 'info',
        // reverseButtons: true,
        showCancelButton: true,
        confirmButtonText: '確認',
        cancelButtonText: '取消',
        customClass: {
          confirmButton: 'sweet-btn-primary',
        },
        ...options,
      };

      try {
        const result = await Swal.fire(defaultOptions);
        return result.isConfirmed;
      } catch (error) {
        console.error('Swal Error:', error);
        return false;
      }
    };
  },
};

export default swalPlugin;
