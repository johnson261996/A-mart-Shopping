<?php
error_reporting(-1);
date_default_timezone_set("Asia/Kolkata");
include "../config/dbconnect.php";
    $error = array();
    $product = array();
	$temp=array();
	$response['error'] = false;
    if ((isset($_POST['cust_id']))) {
        $cust_ID = $_POST['cust_id'];
        $result = mysqli_query($con, "SELECT * FROM purchase_order WHERE Cust_ID = '" . $cust_ID. "' order by Purchase_Date desc");
		$num_rows=mysqli_num_rows($result);
		if($num_rows>0){
			while ($row = mysqli_fetch_array($result)){
				$product['bill_no'] = $row['Bill_nbr'];
				$P_Code = $row['P_Code'];
				$prod_name = mysqli_query($con, "SELECT Name FROM product_master WHERE P_Code = '" . $P_Code. "'");
				if($row_name = mysqli_fetch_array($prod_name))
					$product['p_name'] = $row_name['Name'];
				else{
					$error['message'] = "Oops error occured please try agian later";
					$response['error'] = true;
					$response['error1']=$error;
					break;
				}
				$product['s_qty'] = $row['S_Qty'];
				$product['c_qty'] = $row['Case_Qty'];
				$product['addrss'] = $row['Vendor_Address'];
				$product['status'] = $row['Status'];
				$product['grand_tot'] = $row['Tot_Cost'];
				$product['o_date'] = $row['Purchase_Date'];
				array_push($temp,$product); 	
			}
			if($response['error'] == false){
				$response['order_product'] = $temp;
			}
		}else{
			$error['message'] = "No records found";
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