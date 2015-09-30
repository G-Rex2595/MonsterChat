/* Project:  WiFinder
 * File:  ChatRoom.cpp
 * Author:  Andrew Sytsma
 *
 * [INSERT DESCRIPTION]
 */

/* TODO:  Create description for file
 * TODO:  Code file methods
 * TODO:  Create description for methods
 */

#include "ChatRoom.h"

ChatRoom::ChatRoom(P2PManager manager)
{
	ChatRoom::manager = manager;
}	//end of ChatRoom constructor

vector<Message> ChatRoom::getMessages()
{
	return ChatRoom::msgs;
}	//end of getMessages method

void ChatRoom::sendMessage(Message msg)
{
	ChatRoom::msgs.push_back(msg);
	ChatRoom::manager.sendMessage(msg);
}	//end of sendMessage method

void ChatRoom::addMessage(Message msg)
{
	ChatRoom::msgs.push_back(msg);
	ChatRoom::view.addMessage(msg);
}	//end of addMessage method

void ChatRoom::setRoomName(string roomName)
{
	ChatRoom::roomName = roomName;
}	//end of setRoomName method

bool ChatRoom::failedConnect()
{
	ChatRoom::view.failedConnect();
}	//end of failedConnect method

void ChatRoom::close()
{
	//call LogWriter's close()
}	//end of close method

void ChatRoom::setChatView(ChatView view)
{
	ChatRoom::view = view;
}	//end of setChatView method