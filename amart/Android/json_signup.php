<?php
date_default_timezone_set("Asia/Kolkata");
include "../config/dbconnect.php";

	if((isset($_POST['fname']))&&(isset($_POST['usrname']))&&(isset($_POST['email']))&&(isset($_POST['ctype']))&&(isset($_POST['addrs1']))&&(isset($_POST['addrs2']))&&(isset($_POST['city']))&&(isset($_POST['state']))&&(isset($_POST['phone1']))&&(isset($_POST['phone2']))&&(isset($_POST['latitude']))&&(isset($_POST['longitude']))&&(isset($_POST['password']))){
		$response = array();
		$user = array();
		$error=false;
		$fname = $_POST['fname'];
		$usrname =$_POST['usrname'];
		$email =$_POST['email'];
		$type = $_POST['ctype'];
		$addrss=$_POST['addrs1'];
		$altaddrss=$_POST['addrs2'];
		$city=$_POST['city'];
		$state = $_POST['state'];
		$phone = $_POST['phone1'];
		$altphone=$_POST['phone2'];
		$latitude=$_POST['latitude'];
		$longitude=$_POST['longitude'];
		$password = $_POST['password'];
		
		$username = mysqli_query($con,"Select * from Customer_Master where UserEmail='".$email."' or UserName='".$usrname."' or Phone_1=".$phone." or Phone_2=".$altphone."" );
				if($rows = mysqli_fetch_array($username)){
					
					if($email==$rows['UserEmail']){
						$error = true;
						$error_msg['message'] = "Email  already exist";
						$response['error'] = true;
						$response['message'] = $error_msg;
					}
					if($usrname==$rows['UserName']){
						$error = true;
						$error_msg['message'] = "Username  already exist";
						$response['error'] = true;
						$response['message'] = $error_msg;
					}
					if($phone==$rows['Phone_1']){
						$error = true;
						$error_msg['message'] = "Phone number already exist";
						$response['error'] = true;
						$response['message'] = $error_msg;
					}
					if($altphone==$rows['Phone_2']){
						$error = true;
						$error_msg['message'] = "Alternative Phone number already exist";
						$response['error'] = true;
						$response['message'] = $error_msg;
					}
				}
				
				$id = substr($phone, 5, 5);
				$string_fname = preg_replace('/\s+/', '', $fname);
				$string_fname = str_replace(' ','',$string_fname);
				$custid = $string_fname.$id;
				if (!$error) { 
					$adduser = mysqli_query($con, "INSERT INTO Customer_Master(Cust_Id,Cust_Name,UserName,UserPW,UserEmail,Cust_Type,Address_1,Address_2,City,State,Phone_1,Phone_2,longitude,latitude) VALUES('" .$custid. "','" .$fname. "','".$usrname."','".$password."', '" .$email. "',".$type.",'".$addrss."','".$altaddrss."','".$city."','".$state."',".$phone.",'".$altphone."','".$longitude."','".$latitude."')");
						if($adduser)
						{			   		
							$success_msg['message']="Added user successfully";
							$response['error'] = false;
							$response['message'] = $success_msg;
						}
						else{
							$error_msg['message'] = "Please try again later";
							$response['error'] = false;
							$response['message'] = $error_msg;
						}
				}
	}
	else{
		$error['message'] = "Oops error occured please enter the correct detials"; 
		$response['error'] = true;
		$response['message'] = $error;
	}
	header('Content-Type: application/json');
    echo json_encode($response);
?>