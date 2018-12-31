<?php
error_reporting(-1);
date_default_timezone_set("Asia/Kolkata");
include "../config/dbconnect.php";
    $error = array();
    $product = array();
	$temp=array();
	$URL="http://192.168.225.56/amart/images/";
    if ((isset($_POST['cust_id']))) {
        $cust_ID = $_POST['cust_id'];
        $result = mysqli_query($con, "SELECT * FROM sales_order,customer_master u WHERE u.Cust_ID = '" . $cust_ID. "'");
        
        while ($row = mysqli_fetch_array($result)){
				$cust_type=$row['Cust_Type'];
                $product['p_code'] = $row['P_Code'];
				if($product['p_code']!=null){
					$result1 = mysqli_query($con, "SELECT * FROM product_master WHERE P_Code = '" . $product['p_code']. "'");
					if($row1=mysqli_fetch_array($result1)){
						 $product['p_name'] = $row1['Name'];
						 $product['sgst'] = $row1['SGST'];
						 $product['cgst'] = $row1['CGST'];
						 if($cust_type==1){
							$product['s_price']=$row1['T1_S_Price'];
							$product['case_price']=$row1['T1_CasePrice'];
							
						 }
						 else if($cust_type==2){
							$product['s_price']=$row1['T2_S_Price'];
							$product['case_price']=$row1['T2_CasePrice'];
							
						 }
						 else if($cust_type==3){
							$product['s_price']=$row1['T3_S_Price'];
							$product['case_price']=$row1['T3_CasePrice'];
							
						 }
						 else if($cust_type==4){
							$product['s_price']=$row1['T4_S_Price'];
							$product['case_qty']=$row1['T4_CasePrice'];
							
						 }
						 else if($cust_type==5){
							$product['s_price']=$row1['T5_S_Price'];
							$product['case_price']=$row1['T5_CasePrice'];
							 
						 }
						  	
					}
					
					$product['image'] = $URL.$row1['Img'];
					$product['p_price'] = $row['Amount'];
					$product['s_qty'] = $row['S_Qty'];
					$product['case_qty'] = $row['Case_Qty'];
					
				}
				
				array_push($temp,$product);
				  
			}
			$response['error'] = false;
			$response['cart_product'] = $temp; 
		
		}
    else{
           $error['message'] = "Oops error occured please try agian later";
            $response['error'] = true;
			$response['error1']=$error;
    }
	header('Content-Type: application/json');
    echo json_encode($response);
?>