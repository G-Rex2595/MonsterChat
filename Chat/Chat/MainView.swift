//
//  MainView.swift
//  Chat
//
//  Created by Vishal Gill on 11/27/15.
//  Copyright © 2015 Vishal Gill. All rights reserved.
//

import Foundation
import UIKit

class MainView: UIViewController{
    
    //let appDelegate = UIApplication.sharedApplication().delegate as! AppDelegate

    //var sharedInstance = Singleton()
    
    
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true)

    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.name.text = Singleton.sharedInstance.userName
        self.view.backgroundColor = Singleton.sharedInstance.backgroundColor
        // Do any additional setup after loading the view, typically from a nib.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBOutlet weak var name: UITextField!
    @IBAction func usernameChanged2(sender: UITextField) {
        
        Singleton.sharedInstance.userName = name.text!
    }

    
    
  /*  override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        let vc : ViewTwo = segue.destinationViewController as! ViewTwo
        vc.labelVal = sharedInstance.userName
    }
    */
    


    
}