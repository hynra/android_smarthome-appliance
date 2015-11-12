<?php
	if (!empty($_POST)) {

		$param = $_POST["param"];
		$ip = $_SERVER['HTTP_HOST'];
		switch ($param) {
		 	case 'capture':
		 		header('Location: http://'.$ip.'/cgi-bin/qwertycgi/Capture.cgi');
		 		break;
	 		case 'alarm_off':
		 		header('Location: http://'.$ip.'/cgi-bin/qwertycgi/AlarmOff.cgi');
		 		break;
		 	case 'led_off':
		 		header('Location: http://'.$ip.'/cgi-bin/qwertycgi/LedOff.cgi');
		 		break;
		 	case 'led_on':
		 		header('Location: http://'.$ip.'/cgi-bin/qwertycgi/LedOn.cgi');
		 		break;
		 	case 'alarm_on':
		 		header('Location: http://'.$ip.'/cgi-bin/qwertycgi/AlarmOn.cgi');
		 		break;
		 	default:
		 		# code...
		 		break;

		 };
		 $response["success"] = 1;
		$response["message"] = "Comment Successfully Added!";
		echo json_encode($response);
	}

?>