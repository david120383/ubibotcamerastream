/*! \file AVAPIs.h
This file describes all the APIs of the AV module in IOTC platform.
AV module is a kind of data communication modules in IOTC platform to provide
fluent streaming Audio / Video data from AV servers to AV clients in
unidirectional way.

\copyright Copyright (c) 2010 by Throughtek Co., Ltd. All Rights Reserved.

Revision Table

Version     | Name             |Date           |Description
------------|------------------|---------------|-------------------
3.1.10.5    |Terry Liu         |2018-12-17     |- Mark avServStart, avRecvFrameData as deprecated
3.1.10.6    |Ethan Tsai        |2019-02-11     |- Mark avServStart2, avServStart3 as deprecated
3.1.10.6    |Ethan Tsai        |2019-02-11     |- Mark avClientStart, avClientStart2 as deprecated
 */

// package com.kit.cordova.nativeLocation.com.tutk.IOTC;
package com.tutk.IOTC;

import java.util.ArrayList;

public class AVAPIstest {

    static {
        try {
//             System.loadLibrary("AVAPIs");
            System.loadLibrary("IOTCAPIs");
        }
	    catch(UnsatisfiedLinkError ule){
		    System.out.println("loadLibrarytttttttt(AVAPIs),"+ule.getMessage());
	    }
	}

	public native static int  IOTC_Initialize2(int UDPPort);
//     public static String myLoadLibrary(){
//         try{
//             System.loadLibrary("AVAPIs");
// //             System.loadLibrary("AVAPIsaaaaaa");
//             return "success";
//         }
//         catch(UnsatisfiedLinkError ule){
// //             System.out.println("loadLibrary(AVAPIs),"+ule.getMessage());
//             return "failed:" + ule.getMessage();
//         }
//     }


// 	static { try {System.loadLibrary("AVAPIs");}
// 	catch(UnsatisfiedLinkError ule){
// 		System.out.println("loadLibrary(AVAPIs),"+ule.getMessage());
// 	}
// 	}
}
