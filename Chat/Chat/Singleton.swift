//
//  Singleton.swift
//  Chat
//
//  Created by Vishal Gill on 11/28/15.
//  Copyright © 2015 Vishal Gill. All rights reserved.
//

import Foundation
//import MultipeerConnectivity
import UIKit

class Singleton: UIFont{
    
    var userName = "Anon"
    var roomName = ""
    
    var backgroundColor = UIColor.whiteColor()
    var textColor = UIColor.blueColor()

    var timeStamp = "Standard"
    var font = "Helvetica Neue"
    
    var blockList: [String] = []
    

    static let sharedInstance = Singleton()

}

