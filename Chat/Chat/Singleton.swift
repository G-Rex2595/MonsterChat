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

class Singleton{
    
    var backgroundColor = UIColor.orangeColor()
    var userName = "Anon"
    var roomName = ""
    static let sharedInstance = Singleton()

}

