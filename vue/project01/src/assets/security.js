/**
 * 보안 처리에 공통적으로 사용되는 함수들 모음
 * 
 */

 import router from '@/router/index.js';

 const methods = {
 
    inputMaxChk : (el, maxLength) => {

        if(el != undefined)
            if(el.value.length > maxLength ) {
                el.value = el.value.substr(0, maxLength );
            }
    },

    inputNulChk : (el) => {
        if(el.value.length ==0) {
            alert('값을 추가해주세요')
            return false;
        }
        else 
            return true;
    },

    characterCheck : (el) => {

        /* eslint-disable */
        var regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+┼<>@\#$%&\'\"\\\(\=]/gi; 
        // 허용할 특수문자는 여기서 삭제하면 됨
        if( regExp.test(el.value) ){
            alert("특수문자는 입력하실수 없습니다.");
            //obj.value = obj.value.substring( 0 , obj.value.length - 1 ); // 입력한 특수문자 한자리 지움
            return false;
            }

        else
            return true;
    },

    checkSecurity(el) {
        if(methods.characterCheck(el))
            if(methods.characterCheck(el))
                return true;
            else
                return false;
        else 
            return false;
    }


}

export default {

    install(Vue) {
      Vue.config.globalProperties.$inputMaxChk = methods.inputMaxChk ;
      Vue.config.globalProperties.$checkSecurity = methods.checkSecurity ;
  
    }
}