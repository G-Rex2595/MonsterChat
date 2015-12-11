//
//  Chatlogs.swift
//  Chat
//
//  Created by Vishal Gill on 12/4/15.
//  Copyright © 2015 Vishal Gill. All rights reserved.
//

import Foundation
import UIKit

class Chatlogs: UIViewController
{
    
    @IBOutlet weak var back: UIButton!
    
    @IBOutlet weak var deleteAll: UIButton!

    @IBOutlet weak var logTable: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.back.tintColor = Singleton.sharedInstance.textColor
        self.back.titleLabel!.font = UIFont(name: Singleton.sharedInstance.font, size:(back.titleLabel!.font.pointSize))!
        
        self.deleteAll.tintColor = Singleton.sharedInstance.textColor
        self.deleteAll.titleLabel!.font = UIFont(name: Singleton.sharedInstance.font, size:(deleteAll.titleLabel!.font.pointSize))!
        // Do any additional setup after loading the view, typically from a nib.
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
        
}