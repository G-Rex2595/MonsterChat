import Foundation

class Message
{
    var username: String
    var message: String
    var id: String
    var roomName: String
    var time: NSDate
    
    init(username: String, message: String, id: String, roomName: String)
    {
        self.message = message
        self.username = username
        self.id = id
        self.roomName = roomName
        self.time = NSDate()
    }
    
    func setTime()
    {
        self.time = NSDate()
    }
    
    func getName() -> String
    {
        return self.username
    }
    
    func getMessage() -> String
    {
        return self.message;
    }
    
    func getID() -> String
    {
        return self.id
    }
    
    func getRoomName() -> String
    {
        return self.roomName
    }
    
    func setTime(time: NSDate)
    {
        self.time = time
    }
    
    func setCurrentTime()
    {
        self.time = NSDate()
    }
}

class ChatRoom
{
    var username: String
    var network: P2PManager
    var roomName: String
    var messages: [Message]
    var gui: ChatRoomView?
    
    init(network: P2PManager, username: String, roomName: String)
    {
        self.network = network
        self.username = username
        self.roomName = roomName
        self.messages = []
    }
    
    func sendMessage(text: String) -> Message
    {
        let message = Message(username: self.username, message: text, id: "id", roomName: self.roomName)
        
        self.messages.append(message)
        
        self.network.sendMessage(message)
        
        return message
    }
    
    func getMessages() ->[Message]
    {
        return self.messages
    }
    
    func addMessage(message: Message)
    {
        self.messages.append(message)
        
        self.gui!.addMessage(message)
    }
    
    func exit()
    {
        //TODO: later sprint
    }
    
    func setChatRoomView(view: ChatRoomView)
    {
        self.gui = view;
    }
    
    func setUsername(username: String)
    {
        self.username = username
    }
}

class ChatRoomManager
{
    let network: P2PManager
    var currentRoom: ChatRoom?
    var username: String
    
    init(username: String)
    {
        self.username = username
        self.network = P2PManager.getInstance()
    }
    
    func GetAvailableChatRooms() -> [String]
    {
        return network.getAvailableChatRooms()
    }
    
    func joinRoom(roomName: String) -> ChatRoom
    {
        self.currentRoom!.exit()
        
        self.currentRoom = ChatRoom(network: self.network, username: self.username, roomName: roomName)
        
        return self.currentRoom!
    }
    
    func exit()
    {
        self.currentRoom!.exit()
    }
}

class P2PManager
{
    static let theInstance = P2PManager()
    
    init()
    {
        print("Singleton Created")
    }
    
    static func getInstance() -> P2PManager
    {
        return P2PManager.theInstance
    }
    
    func getAvailableChatRooms() -> [String]
    {
        return ["room"]
    }
    
    func sendMessage(message: Message)
    {
        print("Message Sent")
    }
}

class ChatRoomView
{
    init()
    {
        print("init")
    }
    
    func addMessage(message: Message)
    {
        print("Message Added")
    }
}