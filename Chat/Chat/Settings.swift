//
//  Settings.swift
//  Chat
//
//  Created by Vishal Gill on 12/3/15.
//  Copyright © 2015 Vishal Gill. All rights reserved.
//

import Foundation
import UIKit

class Settings: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource{

    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var blockList: UIButton!
    @IBOutlet weak var colorLabel: UILabel!
    @IBOutlet weak var timestampLabel: UILabel!
    @IBOutlet weak var fontLabel: UILabel!
    @IBOutlet weak var colorPicker: UIPickerView!
    @IBOutlet weak var timestampPicker: UIPickerView!
    @IBOutlet weak var fontPicker: UIPickerView!
    let colorData = ["Default", "DOS", "Nuclear"]
    let timestampData = ["Standard", "Military", "Disable"]
    let fontData = ["Helvetica Neue", "Hiragino Mincho ProN", "Marker Felt", "Papyrus"]
    var currentColor = "Default"
    var currentTimestamp = "Standard"
    var currentFont = "Helvetica Neue"

    override func viewDidLoad() {
        super.viewDidLoad()
        self.colorPicker.delegate = self
        self.colorPicker.dataSource = self
        self.timestampPicker.delegate = self
        self.timestampPicker.dataSource = self
        self.fontPicker.delegate = self
        self.fontPicker.dataSource = self
        
        self.view.backgroundColor = Singleton.sharedInstance.backgroundColor
        self.backButton.tintColor = Singleton.sharedInstance.textColor
        self.blockList.tintColor = Singleton.sharedInstance.textColor
        self.colorLabel.textColor = Singleton.sharedInstance.textColor
        self.timestampLabel.textColor = Singleton.sharedInstance.textColor
        self.fontLabel.textColor = Singleton.sharedInstance.textColor
        
        self.backButton.titleLabel?.font = UIFont(name: Singleton.sharedInstance.font, size:(backButton.titleLabel?.font?.pointSize)!)
        self.blockList.titleLabel?.font = UIFont(name: Singleton.sharedInstance.font, size:(blockList.titleLabel?.font?.pointSize)!)
        self.colorLabel.font = UIFont(name: Singleton.sharedInstance.font, size:(colorLabel.font?.pointSize)!)
        self.timestampLabel.font = UIFont(name: Singleton.sharedInstance.font, size:(timestampLabel.font?.pointSize)!)
        self.fontLabel.font = UIFont(name: Singleton.sharedInstance.font, size:(fontLabel.font?.pointSize)!)
        
        // Do any additional setup after loading the view, typically from a nib.

    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    //MARK: Data Sources
    func numberOfComponentsInPickerView(pickerView: UIPickerView) -> Int {
        return 1
    }
    func pickerView(pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if(pickerView == colorPicker){
            return colorData.count
        }
        else if(pickerView == timestampPicker){
            return timestampData.count
        }
        else{
            return fontData.count
        }
    }
    //MARK: Delegates
    func pickerView(pickerView: UIPickerView, attributedTitleForRow row: Int, forComponent component: Int) -> NSAttributedString? {
        var attributedString: NSAttributedString!

        if(pickerView == colorPicker){
            attributedString = NSAttributedString(string: colorData[row], attributes: [NSForegroundColorAttributeName : Singleton.sharedInstance.textColor,
                NSFontAttributeName:UIFont(name: "Gurmukhi MN", size: 35)!])

            return attributedString
        }
        else if(pickerView == timestampPicker){
            attributedString = NSAttributedString(string: timestampData[row], attributes: [NSForegroundColorAttributeName : Singleton.sharedInstance.textColor,
                NSFontAttributeName:UIFont(name: "Helvetica", size: 90)!])
            
            return attributedString
        }
        else{
            attributedString = NSAttributedString(string: fontData[row], attributes: [NSForegroundColorAttributeName : Singleton.sharedInstance.textColor,
                NSFontAttributeName:UIFont(name: "Helvetica", size: 90)!])
            
            return attributedString
        }
    }
    
    func pickerView(pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        if(pickerView == colorPicker){
            currentColor = colorData[row]
            changeColor(currentColor);
        }
        else if(pickerView == timestampPicker){
            currentTimestamp = timestampData[row]
            changeTimestamp(currentTimestamp);
        }
        else{
            currentFont = fontData[row]
            changeFont(currentFont);
        }
    }
    
    func changeColor(color: String){
        if(color == "DOS"){
            Singleton.sharedInstance.backgroundColor = UIColor.whiteColor()
            Singleton.sharedInstance.textColor = UIColor.blackColor()
        }
        else if(color == "Nuclear"){
            Singleton.sharedInstance.backgroundColor = UIColor.blackColor()
            Singleton.sharedInstance.textColor = UIColor.greenColor()
        }
        else{//color == "Default"
            Singleton.sharedInstance.backgroundColor = UIColor.whiteColor()
            Singleton.sharedInstance.textColor = UIColor.blueColor()
        }
        self.colorPicker.reloadAllComponents()
        self.timestampPicker.reloadAllComponents()
        self.fontPicker.reloadAllComponents()
        //change these now for current page
        self.view.backgroundColor = Singleton.sharedInstance.backgroundColor
        self.backButton.tintColor = Singleton.sharedInstance.textColor
        self.blockList.tintColor = Singleton.sharedInstance.textColor
        self.colorLabel.textColor = Singleton.sharedInstance.textColor
        self.timestampLabel.textColor = Singleton.sharedInstance.textColor
        self.fontLabel.textColor = Singleton.sharedInstance.textColor
    
    }
    
    func changeTimestamp(time: String){
        if(time == "Military"){
            Singleton.sharedInstance.timeStamp = "Military"
        }
        else if(time == "Disable"){
            Singleton.sharedInstance.timeStamp = "Disable"
        }
        else{//time == "Standard"
            Singleton.sharedInstance.timeStamp = "Standard"
        }
    }
    
    func changeFont(font:String){
        if(font == "Hiragino Mincho ProN"){
            Singleton.sharedInstance.font = font
        }
        else if(font == "Marker Felt"){
            Singleton.sharedInstance.font = font
        }
        else if(font == "Papyrus"){
            Singleton.sharedInstance.font = font
        }
        else{//font == "default"
            Singleton.sharedInstance.font = "Helvetica Neue"
        }
        self.backButton.titleLabel?.font = UIFont(name: Singleton.sharedInstance.font, size:(backButton.titleLabel?.font?.pointSize)!)
        self.blockList.titleLabel?.font = UIFont(name: Singleton.sharedInstance.font, size:(blockList.titleLabel?.font?.pointSize)!)
        self.colorLabel.font = UIFont(name: Singleton.sharedInstance.font, size:(colorLabel.font?.pointSize)!)
        self.timestampLabel.font = UIFont(name: Singleton.sharedInstance.font, size:(timestampLabel.font?.pointSize)!)
        self.fontLabel.font = UIFont(name: Singleton.sharedInstance.font, size:(fontLabel.font?.pointSize)!)
    }


    
}