<?php
error_reporting(-1);
date_default_timezone_set("Asia/Kolkata");
include "../config/dbconnect.php";
    $error = array();
    $user = array();
    if ((isset($_POST['email'])) && (isset($_POST['password']))) {
        $email = $_POST['email'];
        $password = $_POST['password'];
        $result = mysqli_query($con, "SELECT * FROM customer_master WHERE UserEmail = '" . $email. "' and UserPW  = '" . $password . "'");
        
        if ($row = mysqli_fetch_array($result)){
            $role = $row['Cust_Type'];
            if($role == '0'){
                $user['user_id'] = $row['Cust_Id'];
                $user['name'] = $row['Cust_Name'];
                $user['email'] = $row['UserEmail'];
                $user['type'] = 'admin';
				$user['usrname']=$row['UserName'];
				$user['phone']=$row['Phone_1'];
				$user['phone1']=$row['Phone_2'];
				$user['addrs']=$row['Address_1'];
				$user['addrs1']=$row['Address_2'];
            }
            else{
                $user['user_id'] = $row['Cust_Id'];
                $user['name'] = $row['Cust_Name'];
                $user['email'] = $row['UserEmail'];
                $user['type'] = 'others';
				$user['usrname']=$row['UserName'];
				$user['phone']=$row['Phone_1'];
				$user['phone1']=$row['Phone_2'];
				$user['addrs']=$row['Address_1'];
				$user['addrs1']=$row['Address_2'];
				
            }
            $response['error'] = false;
            $response['user'] = $user;   
        }
		else{
			$error['message'] = "Oops error occured please enter the correct detials";
            $response['error'] = true;
			$response['error1']=$error;
			
           //  
		}
    }
    else{
           $error['message'] = "Oops error occured please enter the correct detials";
            $response['error'] = true;
			$response['error1']=$error;
    }
	header('Content-Type: application/json');
    echo json_encode($response);
?>