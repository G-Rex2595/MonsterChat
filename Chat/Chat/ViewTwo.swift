//
//  ViewController.swift
//  Chat
//
//  Created by Vishal Gill on 11/26/15.
//  Copyright © 2015 Vishal Gill. All rights reserved.
//

import UIKit
import MultipeerConnectivity

class ViewTwo: UIViewController, MCNearbyServiceAdvertiserDelegate, MCNearbyServiceBrowserDelegate, UITableViewDelegate, UITableViewDataSource {
    
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.resignFirstResponder()
        //self.view.endEditing(true)
        
    }
    
    let serviceType = "Monster-Chat"
    var session : MCSession!
    var peerID: MCPeerID!
    var browser: MCNearbyServiceBrowser!
    var assistant : MCNearbyServiceAdvertiser!
    
    var discoveryInfo = [String: String]()
    var foundPeers = [MCPeerID]()
    var invitationHandler: ((Bool, MCSession)->Void)!
    
    var peerInformation = [peerInfo]()
    
    
    @IBAction func createChat(sender: AnyObject) {
        
        //nameOfRoom.text = "\(Singleton.sharedInstance.userName)'s Chat"
        discoveryInfo["room"] = nameOfRoom.text
        self.peerID = MCPeerID(displayName: Singleton.sharedInstance.userName)
        self.session = MCSession(peer: peerID)
        //self.session.delegate = self
        self.assistant = MCNearbyServiceAdvertiser(peer: peerID, discoveryInfo: discoveryInfo, serviceType: serviceType)
        self.assistant.delegate = self
        
        self.assistant.startAdvertisingPeer()
        self.table.reloadData()
    }
    
    @IBOutlet weak var label: UILabel!
    @IBOutlet weak var table: UITableView!
    @IBOutlet weak var nameOfRoom: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = Singleton.sharedInstance.backgroundColor
        label.text = Singleton.sharedInstance.userName
        
        self.peerID = MCPeerID(displayName: Singleton.sharedInstance.userName)
        self.browser = MCNearbyServiceBrowser(peer: peerID, serviceType: serviceType)
        self.browser.delegate = self
        self.browser.startBrowsingForPeers()
        // Do any additional setup after loading the view, typically from a nib.
        self.table.reloadData()
    }


    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(table: UITableView, numberOfRowsInSection section: Int) -> Int {
        return peerInformation.count
    }
    
    func tableView(table: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let tableCell = UITableViewCell()
        
        if(peerInformation[indexPath.row].discInfo["room"] == ""){
            tableCell.textLabel?.text = "\(peerInformation[indexPath.row].peerID.displayName)'s chatroom"
        }else{
            tableCell.textLabel?.text = peerInformation[indexPath.row].discInfo["room"]!+" chatroom"
        }

        return tableCell
    }
    
    func tableView(table: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        return 60.0
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        let selectedPeer = peerInformation[indexPath.row].peerID
        browser.invitePeer(selectedPeer, toSession: session, withContext: nil, timeout: 20)
    }
    
    func browser(browser: MCNearbyServiceBrowser,
        foundPeer peerID: MCPeerID,
        withDiscoveryInfo info: [String : String]?){
            
        if(peerID.displayName == Singleton.sharedInstance.userName){
            return
        }

        peerInformation.append(peerInfo(peerID: peerID, discInfo: info!))

        self.table.reloadData()
            
    }
    
    func browser(browser: MCNearbyServiceBrowser,
        lostPeer peerID: MCPeerID){
            print("lost peer")

            for (index, peerInfo) in peerInformation.enumerate(){
                if peerInfo.peerID == peerID {
                    peerInformation.removeAtIndex(index)
                    self.table.reloadData()
                    return
                }
            }
    }
    
    func advertiser(advertiser: MCNearbyServiceAdvertiser,
        didReceiveInvitationFromPeer peerID: MCPeerID,
        withContext context: NSData?,
        invitationHandler: (Bool,
        MCSession) -> Void){

            self.invitationHandler = invitationHandler
            let alert = UIAlertController(title: "", message: "\(peerID.displayName) wants to chat with you.", preferredStyle: UIAlertControllerStyle.Alert)
            
            let acceptAction: UIAlertAction = UIAlertAction(title: "Accept", style: UIAlertActionStyle.Default) { (alertAction) -> Void in
                invitationHandler(true, self.session)
                print("accepted")
            }
            
            let declineAction = UIAlertAction(title: "Cancel", style: UIAlertActionStyle.Cancel) { (alertAction) -> Void in
                invitationHandler(false, self.session)
                print("declined")
            }
            
            alert.addAction(acceptAction)
            alert.addAction(declineAction)
            
            NSOperationQueue.mainQueue().addOperationWithBlock { () -> Void in
                self.presentViewController(alert, animated: true, completion: nil)
            }
            
    }
    
    func session(session: MCSession!, peer peerID: MCPeerID!, didChangeState state: MCSessionState) {
        switch state{
            
        case MCSessionState.Connected:
            print("Connected to session: \(session)")
            //NSOperationQueue.mainQueue().addOperationWithBlock { () -> Void in
            //self.performSegueWithIdentifier("idSegueChat", sender: self)
    
        case MCSessionState.Connecting:
            print("Connecting to session: \(session)")
            
        default:
            print("Did not connect to session: \(session)")
        }
    }

}


class peerInfo
{
    var peerID: MCPeerID
    var discInfo: [String: String]
    
    init(peerID: MCPeerID, discInfo: [String: String])
    {
        self.peerID = peerID
        self.discInfo = discInfo
    }
    
}
