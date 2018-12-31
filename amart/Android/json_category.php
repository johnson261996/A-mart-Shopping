<?php
error_reporting(-1);
date_default_timezone_set("Asia/Kolkata");
include "../config/dbconnect.php";
    //$error = array();
    
	$temp=array();
	$response= array();
        $result = mysqli_query($con, "SELECT * FROM Category");
        while ($row = mysqli_fetch_array($result)){
				$category = array();
				$j=1;
			    $cat=$row['Cat_Name'];
				for($i=0;$i<=2;$i++){
				    $temp['sub_cat']=$row['Sub_Cat'.$j];
					array_push($category,$temp);
					$j++;
				}
				$j=0;
				$response[$cat]=$category;
				unset($category);
				
				
        }
	header('Content-Type: application/json');
    echo json_encode($response);
?>