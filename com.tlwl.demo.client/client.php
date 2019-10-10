<?php
require_once "Ice.php";
require_once "RequestI.php";

use IRequest\RequestPrxHelper;

$ice = null;
try {
    $communicator = Ice\initialize();
    $base = $communicator->stringToProxy("tlwladapter:default -p 18001");
    // $request = RequestPrxHelper::checkedCast($base);
    $request = $base->ice_checkedCast('::IRequest::Request', null, null);
    if (!$request) {
        throw new RuntimeException("Invalid proxy"); 
    }
    $result_json=$request->setRequest("book_list","{id:2}");
    echo $result_json;
    $array=json_decode($result_json);
    echo "\r\n".str_repeat("---",40)."\r\n";
    echo "|\r\n|".str_repeat("\t",7)." The Book List \r\n|\r\n";
    foreach ($array->data as $val) {
        $str = str_repeat("---",40)."\r\n|id:\r\t{$val->id}\r\n|isbn:\t{$val->isbn}\r\n|title:\t{$val->title}\r\n";
        echo $str;
        // echo "\r\tisbn:{$val->isbn}\r\n";
        // echo "\r\ttitle:{$val->title}\r\n";
    }
    echo str_repeat("---",40);
    // var_dump($array->data);
} catch (Exception $ex) {
    echo "\n{$ex}\n";
}
if($ice){
    $ice->destroy();
}