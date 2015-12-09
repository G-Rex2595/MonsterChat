//
//  BlockListView.swift
//  Chat
//
//  Created by Vishal Gill on 12/4/15.
//  Copyright © 2015 Vishal Gill. All rights reserved.
//

import Foundation
import UIKit

class BlockListView: UIViewController, UITableViewDelegate, UITableViewDataSource
{


    @IBOutlet weak var blockListTable: UITableView!
    @IBOutlet weak var settings: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.settings.tintColor = Singleton.sharedInstance.textColor
        self.settings.titleLabel!.font = UIFont(name: Singleton.sharedInstance.font, size:(settings.titleLabel!.font.pointSize))!
        
        
        self.blockListTable.delegate = self
        self.blockListTable.dataSource = self
        // Do any additional setup after loading the view, typically from a nib.
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    
    func numberOfSectionsInTableView(blockListTable: UITableView) -> Int {
        return 1;
    }
    
    func tableView(blockListTable: UITableView, numberOfRowsInSection section: Int) -> Int {
        return Singleton.sharedInstance.blockList.count
    }
    
    func tableView(blockListTable: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let tableCell = UITableViewCell()
        tableCell.textLabel?.text = Singleton.sharedInstance.blockList[indexPath.row]
        
        return tableCell
    }
    
    func tableView(blockListTable: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        return 35.0
    }
    
//    func tableView(blockListTable: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
//        
//        Singleton.sharedInstance.blockList.removeAtIndex(indexPath.row)
//        
//        blockListTable.reloadData()
//        
//        
//   
//    }
    
    func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
        if editingStyle == UITableViewCellEditingStyle.Delete {
            Singleton.sharedInstance.blockList.removeAtIndex(indexPath.row)
            blockListTable.deleteRowsAtIndexPaths([indexPath], withRowAnimation: UITableViewRowAnimation.Automatic)
        }
        
    }




}