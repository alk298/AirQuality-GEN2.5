<?php

 $db_name = "esp";  
 $mysql_user = "root";  
 $mysql_pass = "XXXXXXXXXX"; // your owner password
 $server_name = "localhost:3307";


$con = mysqli_connect($server_name,$mysql_user,$mysql_pass,$db_name); 



$mac = ($_GET['mac']); 
$pm25 = ($_GET['pm25']);
$co2 = ($_GET['co2']);
$temp = ($_GET['t']);
$hum = ($_GET['h']);

date_default_timezone_set('Asia/Taipei');  #設定時區
$nowdt = date("Y-m-d H:i:s");               #儲存時間
 
//UPDATE `esp2`
$sql_check_mac = "SELECT COUNT(1) FROM `esp2` WHERE `mac` = '$mac'";
$check_result = mysqli_query($con, $sql_check_mac) or die("Error in Selecting " . mysqli_error($con));
$check_result = mysqli_fetch_row($check_result);
$check_result = $check_result[0];
	
if($check_result == 0){
	$sql_esp2 = "INSERT INTO `esp2` ( `time`, `temp`, `hum`, `mac`, `pm25`, `co2`) VALUES('$nowdt','$temp','$hum','$mac','$pm25','$co2')";
}else{
	if($check_result == 1){
		$sql_esp2 = "UPDATE `esp2` SET `time` = '$nowdt',`temp`='$temp',`hum`='$hum',`pm25`='$pm25',`co2`='$co2' WHERE `mac`='$mac'"; 
	}else{
		echo "Please check mac address.";
	}
}
	$result_esp2 = mysqli_query($con, $sql_esp2) or die("Error in Selecting " . mysqli_error($con));

//INSERT `esp1`
$sql= " INSERT INTO `esp1` ( `time`, `temp`, `hum`, `mac`, `pm25`, `co2`) 
   VALUES('$nowdt ','$temp','$hum','$mac','$pm25','$co2');"; 

$result = mysqli_query($con, $sql) or die("Error in Selecting " . mysqli_error($con));

 
if($result_esp2){
 echo "esp 2 Insert Success....";
}else{
echo "esp2 Data insert error.....".mysqli_error($con);
}
if($result){
 echo "esp 1 Insert Success....";
}else{
echo "esp1  Data insert error.....".mysqli_error($con);
}
mysqli_close($con);



?>
