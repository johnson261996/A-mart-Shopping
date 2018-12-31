<?php
error_reporting(-1);
date_default_timezone_set("Asia/Kolkata");
include "../config/dbconnect.php";
header('Content-Type: application/json');
    $error = array();
	$qtyCheck=false;
	$response['error'] = false;
    if ((isset($_POST['cart_data']))&&(isset($_POST['cust_id']))) {
			$cust_ID=$_POST['cust_id'];
			$json=$_POST['cart_data'];
			$data=json_decode($json);
			$result= mysqli_query($con, "SELECT * FROM customer_master WHERE Cust_ID = '" . $cust_ID. "'");
			if($row = mysqli_fetch_array($result)){
				$vender_name=$row['Cust_Name'];
				$vender_addrs=$row['Address_1'];
				$vender_phone=$row['Phone_1'];
				$id=rand();
				$string_Bill_Id="BILL";
				$Bill_ID=$string_Bill_Id.$id;
				
				foreach($data as $obj){
					$code = rand();
					$string_p_code = "PUR";
					$purchase_id = $string_p_code.$code;
					$p_code=$obj->p_code;
					$p_s_qty=$obj->p_s_qty;
					$p_case_qty=$obj->p_case_qty;
					$tot_cost=$obj->tot_cost;
					$quantity=mysqli_query($con, "SELECT Case_qty FROM product_master WHERE P_Code = '" . $p_code. "'");
					if($qty=mysqli_fetch_array($quantity)){
						$cqty=$qty['Case_qty'];
						if(( $cqty< $p_case_qty) or ($cqty==0)){
							$response['error'] = true;
							$qtyCheck=true;
							break;
						}
					}
					if(!$qtyCheck){
						$insert_order = mysqli_query($con, "INSERT INTO purchase_order(Purchase_Code,Cust_ID,Bill_nbr,P_Code,Vendor_Name,Vendor_Address,Vendor_Phone,S_Qty,Case_Qty,Tot_Cost,Status)values('" .$purchase_id. "','".$cust_ID."','".$Bill_ID."','".$p_code."','".$vender_name."','".$vender_addrs."','".$vender_phone."','".$p_s_qty."','".$p_case_qty."','".$tot_cost."','Pending')");
						if(!$insert_order){
							$response['error'] = true;
							break;
						}
						else{
							$tot_qty=$cqty-$p_case_qty;
							$result = mysqli_query($con, "update product_master set Case_qty='".$tot_qty."' where P_Code='".$p_code."'");
						}
					}
				}
				if($response['error'] == true){
					$error['message'] = "Oops error occured please try agian later";
					$response['error1']=$error;
				}
				else{
					$response['error'] = false;
					$success['message'] = "Product purchased succesfully";
					$response['error1']=$success;
				}
			}
			else{
				$error['message'] = "Oops error occured please try agian later";
				$response['error'] = true;
				$response['error1']=$error;
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