//
//  MainView.swift
//  Chat
//
//  Created by Vishal Gill on 11/27/15.
//  Copyright © 2015 Vishal Gill. All rights reserved.
//

import Foundation
import UIKit

class MainView: UIViewController, UITextFieldDelegate{

    override func viewDidLoad() {
        super.viewDidLoad()
        self.name.text = Singleton.sharedInstance.userName
        self.view.backgroundColor = Singleton.sharedInstance.backgroundColor
        self.settings.tintColor = Singleton.sharedInstance.textColor
        self.monsterchat.tintColor = Singleton.sharedInstance.textColor
        self.chatrooms.tintColor = Singleton.sharedInstance.textColor
        self.chatlogs.tintColor = Singleton.sharedInstance.textColor
        self.name.textColor = Singleton.sharedInstance.textColor
        
        self.settings.titleLabel?.font = UIFont(name: Singleton.sharedInstance.font, size:(settings.titleLabel?.font?.pointSize)!)
        self.monsterchat.titleLabel?.font = UIFont(name: Singleton.sharedInstance.font, size:(monsterchat.titleLabel?.font?.pointSize)!)
        self.chatrooms.titleLabel?.font = UIFont(name: Singleton.sharedInstance.font, size:(chatrooms.titleLabel?.font?.pointSize)!)
        self.chatlogs.titleLabel?.font = UIFont(name: Singleton.sharedInstance.font, size:(chatlogs.titleLabel?.font?.pointSize)!)
        self.name.font = UIFont(name: Singleton.sharedInstance.font, size:(name.font?.pointSize)!)


        self.name.delegate = self
        // Do any additional setup after loading the view, typically from a nib.
    
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBOutlet weak var settings: UIButton!
    @IBOutlet weak var monsterchat: UIButton!
    @IBOutlet weak var chatrooms: UIButton!
    @IBOutlet weak var chatlogs: UIButton!
    
    @IBOutlet weak var name: UITextField!
    @IBAction func usernameChanged2(sender: UITextField) {
        
        Singleton.sharedInstance.userName = name.text!
    }

    func textFieldShouldReturn(name: UITextField) -> Bool {
        self.view.endEditing(true)
        return true
    }
    
  /*  override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        let vc : ViewTwo = segue.destinationViewController as! ViewTwo
        vc.labelVal = sharedInstance.userName
    }
    */
    


    
}