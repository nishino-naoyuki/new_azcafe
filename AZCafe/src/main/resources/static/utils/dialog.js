
$("#dialog-message").hide();

//メッセージ表示
const showMessage = (type,message) =>{
	if( type == "success"){
		$(".dialog-success").show();
		$(".dialog-info").hide();
		$(".dialog-error").hide();
	}else if( type == "info"){
		$(".dialog-success").hide();
		$(".dialog-info").show();
		$(".dialog-error").hide();
	}else if( type == "error"){
		$(".dialog-success").hide();
		$(".dialog-info").hide();
		$(".dialog-error").show();
	}
	$("#dialog-text-message").text(message);
	$("#dialog-message-btn").click();
   }