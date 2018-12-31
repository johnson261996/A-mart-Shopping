
<?php
date_default_timezone_set("Asia/Kolkata");
include "../config/dbconnect.php";
	$temp= array();
    $URL="http://192.168.225.56/amart/images/";
    $response = array();
    $products = array();
	//$subcat_name = "pen";
    if ((isset($_POST['name']))&&(isset($_POST['subcat']))&&(isset($_POST['cust_id']))) {
        $subcat_name = $_POST['name'];
        $subcat= $_POST['subcat'];
		$cust_id=$_POST['cust_id'];
		 $result1 = mysqli_query($con, "SELECT * FROM customer_master WHERE Cust_Id = '" . $cust_id. "'");
		 if ($row1 = mysqli_fetch_array($result1)){
			$cust_type=$row1['Cust_Type'];
		 }
        if($subcat==0){
            $result = mysqli_query($con, "SELECT * FROM product_master WHERE SubCat1 = '" . $subcat_name. "'");
        }
        else if($subcat==1){
            $result = mysqli_query($con, "SELECT * FROM product_master WHERE SubCat2 = '" . $subcat_name. "'");
        }
        else if($subcat==2){
            $result = mysqli_query($con, "SELECT * FROM product_master WHERE SubCat3 = '" . $subcat_name. "'");
        }
       while ($row = mysqli_fetch_array($result)){
				$products['product_id'] = $row['P_Code'];
                $products['product_name'] = $row['Name'];
                if($cust_type==1){
                     $products['price'] = $row['T1_S_Price'];
                   // $products['']=$row[''];
					 $products['image'] = $URL.$row['Img'];
				}
				else if($cust_type==2){
                    $products['price'] = $row['T2_S_Price'];
                    //$products['']=$row[''];
					$products['image'] = $URL.$row['Img'];
				}
				else if($cust_type==3){
                    $products['price'] = $row['T3_S_Price'];
                    //$products['']=$row[''];
					$products['image'] = $URL.$row['Img'];
				}
				else if($cust_type==4){
                    $products['price'] = $row['T4_S_Price'];
                    //$products['']=$row[''];
					$products['image'] = $URL.$row['Img'];
				}
				else if($cust_type==5){
                    $products['price'] = $row['T4_S_Price'];
                    //$products['']=$row[''];
					$products['image'] = $URL.$row['Img'];
				}
            array_push($temp,$products);   
        }
   $response['products']=$temp;
	}
	header('Content-Type: application/json');
    echo json_encode($response);
?>