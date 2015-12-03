//
//  ChatView.swift
//  Chat
//
//  Created by Vishal Gill on 12/2/15.
//  Copyright © 2015 Vishal Gill. All rights reserved.
//

import Foundation
import UIKit


class ChatView: UIViewController{


    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true)
        
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = Singleton.sharedInstance.backgroundColor


        // Do any additional setup after loading the view, typically from a nib.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}