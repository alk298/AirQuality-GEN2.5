<?php

$db_name = "esp";  
 $mysql_user = "root";  
 $mysql_pass = "XXXXXXXXXX"; // your owner password
 $server_name = "localhost:3307";

       $con = mysqli_connect($server_name,$mysql_user,$mysql_pass,$db_name)or die("Error " . mysqli_error($con));  


  $sql = "SELECT time,temp,hum,mac,pm25,co2,location FROM `esp2` ORDER BY `esp2`.`id` ASC ;";  




      $result = mysqli_query($con, $sql) or die("Error in Selecting " . mysqli_error($con));
	  
	  

    //create an array
    $emparray = array();
    while($row =mysqli_fetch_assoc($result))
    {
        $emparray[] = $row;
    }
    echo json_encode($emparray);

    //close the db connection
    mysqli_close($con);
 
 
 
 
 ?>  