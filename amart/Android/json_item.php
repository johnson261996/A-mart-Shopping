<?php
error_reporting(-1);
date_default_timezone_set("Asia/Kolkata");
include "../config/dbconnect.php";
    $error = array();
    $product = array();
	$URL="http://192.168.225.56/amart/images/";
    if ((isset($_POST['product_id']))) {
        $product_ID = $_POST['product_id'];
        $result = mysqli_query($con, "SELECT * FROM product_master WHERE P_Code = '" . $product_ID. "'");
        
        if ($row = mysqli_fetch_array($result)){
                $product['prod_name'] = $row['Name'];
				//$product['prod_price'] = $row['Name'];
                $product['stock'] = $row['Case_qty'];
				$product['prod_desc'] = $row['Descr'];
				 $product['img'] = $URL.$row['Img'];
				$response['error'] = false;
				$response['product'] = $product;   
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