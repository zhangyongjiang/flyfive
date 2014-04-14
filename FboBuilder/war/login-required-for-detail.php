<html>
<head>
<script type="text/javascript">
	function login() {
		top.location = '<?php echo FB::getInstance()->getLoginUrl(array("id" => $_REQUEST['id']));?>';
	}
</script>
</head>
<body>
	<a href='#' onclick='login()'>Login to Facebook to see the flyer.</a>
</body>
</html>