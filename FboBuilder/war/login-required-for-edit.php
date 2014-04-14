<html>
<head>
<script type="text/javascript">
	function login() {
		top.location = '<?php echo FB::getInstance()->getLoginUrl(array("page" => "edit"));?>';
	}
</script>
</head>
<body>
	<a href='#' onclick='login()'>Login to Facebook to create your own flyer</a>
</body>
</html>