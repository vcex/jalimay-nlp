$(function() {
	$("li.btn").click(function() {
		console.log($(this).attr("value"));
		$.ajax({
			url : "classify.do",
			type : "post",
			data : {
				"word" : $(this).attr("value")
			},
			success : function(r) {
				$("#classify").html(r);
			}
		});
	});
});