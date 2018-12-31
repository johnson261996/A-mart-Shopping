<?php
error_reporting(-1);
date_default_timezone_set("Asia/Kolkata");
include "../config/dbconnect.php";
    $error = array();
	$success=array();
    if ((isset($_POST['product_id']))&&(isset($_POST['user_id']))&&(isset($_POST['S_Qty']))&&(isset($_POST['Case_Qty']))) {
        $product_ID = $_POST['product_id'];
		$user_ID=$_POST['user_id'];
		$S_Qty=$_POST['S_Qty'];
		$Case_Qty=$_POST['Case_Qty'];
		$id = rand();
		$string_pname = "OID";
		$prod_id = $string_pname.$id;
		$result1 = mysqli_query($con, "SELECT * FROM sales_order WHERE P_Code = '" . $product_ID. "' and Cust_ID='".$user_ID."'");
        
        if ($row = mysqli_fetch_array($result1)){
					$error['message'] = "Already added to cart";
					$response['error'] = true;
					$response['error1']=$error;
		}
		else{
			$price = mysqli_query($con, "SELECT u.Cust_Type,p.T1_S_Price,p.T1_CasePrice,p.T2_S_Price,p.T2_CasePrice,p.T3_S_Price,p.T3_CasePrice,p.T4_S_Price,p.T4_CasePrice,p.T5_S_Price,p.T5_CasePrice FROM Customer_Master u,Product_Master p WHERE p.P_Code = '" . $product_ID. "' and u.Cust_ID='".$user_ID."'");
			if($row=mysqli_fetch_array($price)){
				$cust_type=$row['Cust_Type'];
				
			 if($cust_type==1){
                     $S_Price = $row['T1_S_Price'];
					 $Case_Price = $row['T1_CasePrice'];
				}
				else if($cust_type==2){
                    $S_Price = $row['T2_S_Price'];
					 $Case_Price = $row['T2_CasePrice'];
				}
				else if($cust_type==3){
                    $S_Price = $row['T3_S_Price'];
					 $Case_Price = $row['T3_CasePrice'];
				}
				else if($cust_type==4){
                    $S_Price = $row['T4_S_Price'];
					 $Case_Price = $row['T4_CasePrice'];
				}
				else if($cust_type==5){
                   $S_Price = $row['T4_S_Price'];
					 $Case_Price = $row['T4_CasePrice'];
				}
				$T_Price=(($S_Qty*$S_Price)+($Case_Qty*$Case_Price));
				$result = mysqli_query($con, "INSERT INTO sales_order(Order_ID,Cust_ID,P_Code,S_Qty,Case_Qty,Amount)values( '" . $prod_id. "','".$user_ID."','".$product_ID."','".$S_Qty."','".$Case_Qty."','".$T_Price."')");
				if ($result){
					$success['message'] = "Added to cart successfully";
					$response['error'] = false;
					$response['error1']=$success; 
				}
				else{
					$error['message'] = "Oops error occured please try agian later";
					$response['error'] = true;
					$response['error1']=$error;
				}
			}
		}
	}
    else{
           $error['message'] = "Oops error occured please try agian later";
            $response['error'] = true;
			$response['error1']=$error;
    }
	header('Content-Type: application/json');
    echo json_encode($response);
?>