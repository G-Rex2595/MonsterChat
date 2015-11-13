//
//  ViewController.swift
//  MultipeerChat
//
//  Created by Vishal Gill on 10/10/15.
//  Copyright © 2015 Vishal Gill. All rights reserved.
//

import UIKit
import MultipeerConnectivity

class ViewController: UIViewController, MCBrowserViewControllerDelegate,
MCSessionDelegate ,UIPickerViewDelegate,UIPickerViewDataSource,UITableViewDelegate,UIImagePickerControllerDelegate,UINavigationControllerDelegate{
    
    
    
    @IBOutlet var timeStampPicker: UIPickerView!
    
    let serviceType = "LCOC-Chat"
    
    var browser : MCBrowserViewController!
    var assistant : MCAdvertiserAssistant!
    var session : MCSession!
    var peerID: MCPeerID!
    
    @IBOutlet var tableview: UITableView!
    
    var isEnable = 1
    var isMilitary = 0 //  military   24:00
    var timeStampIndex = 0
    
    var timeStampChoice = ["standard time","military time","disable"]
   // var picker = UIPickerView()
    
    @IBOutlet var chatView: UITextView!
    @IBOutlet var messageField: UITextField!
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true)
    }
    var imag2 = UIImage()
    var cellContent:[NSObject] = []
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return cellContent.count
    }
    
    func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat
    {
        if cellContent[indexPath.row] is String
        {
            return 30
        }
        else
        {
            return 120
        }
  
    }
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell
    {
        let cell = UITableViewCell(style: UITableViewCellStyle.Default, reuseIdentifier: "Cell")
        
        if cellContent[indexPath.row] is String
        {
            cell.textLabel?.text = String(cellContent[indexPath.row])
            
        }
        else
        {
            
            cell.imageView?.image = cellContent[indexPath.row] as! UIImage
            
        }
        return cell
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.peerID = MCPeerID(displayName: UIDevice.currentDevice().name)
        self.session = MCSession(peer: peerID)
        self.session.delegate = self
        
        // create the browser viewcontroller with a unique service name
        self.browser = MCBrowserViewController(serviceType:serviceType,
            session:self.session)
        
        self.browser.delegate = self;
        
        self.assistant = MCAdvertiserAssistant(serviceType:serviceType,
            discoveryInfo:nil, session:self.session)
        
        // tell the assistant to start advertising our fabulous chat
        self.assistant.start()
        
//        timeStampPicker.delegate = self
//        
//        timeStampPicker.dataSource = self
        
    }
       override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    func numberOfComponentsInPickerView(pickerView: UIPickerView) -> Int
    {
        return 1
    }
    
    func pickerView(pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int
    {
        return 3
    }
    
    func pickerView(pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String?
    {
        return timeStampChoice[row]
    }
    
//    @IBAction func save(sender: AnyObject) {
//        if (timeStampIndex == 0)
//        {
//            isEnable = 1
//            isMilitary = 0 // standard
//        }
//        else if(timeStampIndex == 1)
//        {
//            isEnable = 1
//            isMilitary = 1 // military
//        }
//        else
//        {
//            isEnable = 0 // disenable
//        }
//
//    }
    func pickerView(pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        timeStampIndex = row
        if (timeStampIndex == 0)
                {
                    isEnable = 1
                    isMilitary = 0 // standard
                }
                else if(timeStampIndex == 1)
                {
                    isEnable = 1
                    isMilitary = 1 // military
                }
                else
                {
                    isEnable = 0 // disenable
                }
    }
    
    @IBAction func sendChat(sender: UIButton) {
        // Bundle up the text in the message field, and send it off to all
        // connected peers
        
        let msg = self.messageField.text!.dataUsingEncoding(NSUTF8StringEncoding,
            allowLossyConversion: false)
        
        
        let msg1:[String:NSObject] = ["type":"text","data":self.messageField.text!]


        
        print(msg1)
        
        do {
            
            let js = try NSJSONSerialization.dataWithJSONObject(msg1, options: .PrettyPrinted)
            try self.session.sendData(js, toPeers: self.session.connectedPeers, withMode: MCSessionSendDataMode.Unreliable)
        } catch {
            
        }
        
        self.updateChat(self.messageField.text!, fromPeer: self.peerID)
        
        self.messageField.text = ""
    }
    
    func updateChat(text : String, fromPeer peerID: MCPeerID) {
        // Appends some text to the chat view
        
        // If this peer ID is the local device's peer ID, then show the name
        // as "Me"
        var name : String
        
        switch peerID {
        case self.peerID:
            name = "Me"
        default:
            name = peerID.displayName
        }
        
        if isEnable == 1
        {
            if isMilitary == 0
            {
                var todaysDate:NSDate = NSDate()
                var dateFormatter:NSDateFormatter = NSDateFormatter()
                dateFormatter.dateFormat = "yyyy-MM-dd HH:mm"
                var DateInFormat:String = dateFormatter.stringFromDate(todaysDate)
                
                let message = "\(name): \(text)\t\t\(DateInFormat)\n"
                cellContent.append(message)
                self.tableview.reloadData()
            }
            else if isMilitary == 1// standard time
            {
                
                let date = NSDate()
                let calendar = NSCalendar.currentCalendar()
                let components = calendar.components([.Hour, .Minute], fromDate: date)
                var hour = components.hour
                let minutes = components.minute
                
                var amOrPm: String = "pm"
                if hour < 12
                {
                    amOrPm = "am"
                }
                else
                {
                    amOrPm = "pm"
                    
                    hour = hour - 12
                }
                // Add the name to the message and display it
                let message = "\(name): \(text)\t\t\(hour):\(minutes) \(amOrPm)\n"
            }
            else
            {
                let message = "\(name): \(text)\n"
                cellContent.append(message)
                self.tableview.reloadData()

            }
        }

    }
    
    @IBAction func showBrowser(sender: UIButton) {
        // Show the browser view controller
        self.presentViewController(self.browser, animated: true, completion: nil)
    }
    
    func browserViewControllerDidFinish(
        browserViewController: MCBrowserViewController)  {
            // Called when the browser view controller is dismissed (ie the Done
            // button was tapped)
            
            self.dismissViewControllerAnimated(true, completion: nil)
    }
    
    func browserViewControllerWasCancelled(
        browserViewController: MCBrowserViewController)  {
            // Called when the browser view controller is cancelled
            
            self.dismissViewControllerAnimated(true, completion: nil)
    }
    
    
    func session(session: MCSession, didReceiveData data: NSData,
        fromPeer peerID: MCPeerID)  {
            // Called when a peer sends an NSData to us
            
            // This needs to run on the main queue
            do{
                
                let jsonData = try NSJSONSerialization.JSONObjectWithData(data, options: NSJSONReadingOptions.AllowFragments)
                
                let dictionary=jsonData as! NSDictionary
                
                dispatch_async(dispatch_get_main_queue()) {
                    print(data)
                    
                    let weather=dictionary["type"] as! String
                    
                    let username = peerID.displayName
                    
                    if(weather == "image"){
                        
                        let dat = dictionary["data"] as! String
                        
                        let data = NSData(base64EncodedString: dat, options: NSDataBase64DecodingOptions(rawValue: 0))
                        let imag = UIImageView(frame: CGRectMake(30, 0, self.view.bounds.width, 200))
                        imag.image = UIImage(data: data!)
                        self.cellContent.append(imag)
                        self.tableview.reloadData()
                    }else
                        
                    {
                        let msg = dictionary["data"] as! String
                        self.updateChat(msg as String, fromPeer: peerID)
                        
                    }
                    
                }
            }catch{
                
            }

    }
    
    

    // The following methods do nothing, but the MCSessionDelegate protocol
    // requires that we implement them.
    func session(session: MCSession,
        didStartReceivingResourceWithName resourceName: String,
        fromPeer peerID: MCPeerID, withProgress progress: NSProgress)  {
            
            // Called when a peer starts sending a file to us
    }
    
    func session(session: MCSession,
        didFinishReceivingResourceWithName resourceName: String,
        fromPeer peerID: MCPeerID,
        atURL localURL: NSURL, withError error: NSError?)  {
            // Called when a file has finished transferring from another peer
    }
    
    func session(session: MCSession, didReceiveStream stream: NSInputStream,
        withName streamName: String, fromPeer peerID: MCPeerID)  {
            // Called when a peer establishes a stream with us
    }
    
    func session(session: MCSession, peer peerID: MCPeerID,
        didChangeState state: MCSessionState)  {
            // Called when a connected peer changes state (for example, goes offline)
            
    }
    
    @IBAction func pic_clicked(sender: AnyObject) {
        var image = UIImagePickerController()
        image.delegate = self
        image.sourceType = UIImagePickerControllerSourceType.PhotoLibrary
        self.presentViewController(image, animated: true, completion: nil)
    }
    
    func imagePickerController(picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : AnyObject]) {
        
        self.dismissViewControllerAnimated(true) { () -> Void in
            
        }
        let name = info["UIImagePickerControllerOriginalImage"] as! UIImage
        let data = UIImagePNGRepresentation(name)
        let name1 = UIImageJPEGRepresentation(name, 0.3)
        let string1 = name1!.base64EncodedStringWithOptions(NSDataBase64EncodingOptions(rawValue: 0))
        
        
        let imag = UIImageView(frame: CGRectMake(30, 30, self.view.bounds.width-200, 200))
        imag.image = name
        
        self.imag2 = name
        let msg:[String:NSObject] = ["type":"image","data":string1]
        
        do {
            let js = try NSJSONSerialization.dataWithJSONObject(msg, options: .PrettyPrinted)
            try self.session.sendData(js, toPeers: self.session.connectedPeers, withMode: MCSessionSendDataMode.Unreliable)
        } catch {
            
        }
        updateChat("", fromPeer: self.peerID)
        cellContent.append(name)
        self.tableview.reloadData()
        self.messageField.text = ""
    }
    
}

