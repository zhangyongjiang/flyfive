<?php require_once 'php/common_include.php';?>
<!doctype html>

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Flyer Five</title>
    <script type="text/javascript" src="fbobuilder/fbobuilder.nocache.js"></script>
    <script type="text/javascript">
        function onGwtReady() {
            try {
                var f5 = new com.gaoshin.fbobuilder.client.model.CanvasContainer();
                f5.setContainerId("f5container");
                f5.createStage(400, 400);
                var layer = f5.createLayer();
                f5.createNewNode("com.gaoshin.fbobuilder.client.model.Fcircle", layer, null); 
            }
            catch (e) {
                alert(e);
            }
        }
    </script>
  </head>
  <body style="width:100%;margin:0;padding:0;" >
    <div id="f5container" "></div>
  </body>
</html>

