package com.tlwl.demo.ice.client;

import com.tlwl.demo.ice.IRequest.RequestPrx;

public class Client {

    public static void main(String[] args) {
        int status = 0;
        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "config.client"))
        {
            communicator.getProperties().setProperty("Ice.Default.Package", "com.tlwl.demo");
            // RequestPrx requestPrx = RequestPrx.checkedCast(communicator.propertyToProxy("TlwlAdapter.Proxy"));
            RequestPrx requestPrx = RequestPrx.checkedCast(communicator.stringToProxy("tlwladapter:default -h 127.0.0.1 -p 18001"));

            if(requestPrx == null)
            {
                System.err.println("invalid proxy");
                status = 1;
            }else{
                String result = requestPrx.setRequest("book_list", "{}");
                System.out.println(result);
            }
        }
        System.exit(status);
    }
}