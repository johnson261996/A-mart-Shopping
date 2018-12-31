<?php
date_default_timezone_set("Asia/Kolkata");
include "../config/dbconnect.php";
    $response = array();
	if((isset($_POST['password']))&&(isset($_POST['cust_id']))&&(isset($_POST['newpassword']))){
		$password = $_POST['password'];
		$user_ID = $_POST['cust_id'];
		$newPassword=$_POST['newpassword'];
		$result = mysqli_query($con, "SELECT UserPW FROM customer_master WHERE Cust_Id = '" . $user_ID . "'"); 
		if ($row = mysqli_fetch_array($result)){
						if($password == $row['UserPW']){
							$result = mysqli_query($con, "update customer_master set UserPW='".$password."' WHERE Cust_Id = '" . $user_ID . "'");
							$success_msg["message"]="Password updated successfully";
							$response["error"]=false;
							$response["message"]=$success_msg;
						}
						else{
							$error_msg["message"]="Password did not match";
							$response["error"]=true;
							$response["message"]=$error_msg;
						}
			}
				else{
					$error_msg["message"]="Oops! somthing went wrong";
					$response["error"]=true;
					$response["message"]=$error_msg;
				}
		}
		 else{
			$error_msg["message"]="Oops! somthing went wrong";
			$response["error"]=true;
			$response["message"]=$error_msg;
		 } 
	header("Content-Type: application/json");
    echo json_encode($response);
?>