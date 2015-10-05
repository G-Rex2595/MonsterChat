#ifndef CHATROOMMANAGER_CPP
#define CHATROOMMANAGER_CPP

#include<string>
#include<vector>
#include<memory>
#include"ChatRoomManager.h"

ChatRoomManager::ChatRoomManager(std::string uName)
{
	//set username
	this->username = uName;

	//TODO: Start P2PManager
}

std::vector<std::string> ChatRoomManager::getAvailableRooms()
{
	//TODO: Get P2PManager's available rooms

	return std::vector<std::string>();
}

void ChatRoomManager::setUsername(std::string uName)
{
	this->username = uName;
}

std::shared_ptr<ChatRoom> ChatRoomManager::joinRoom(std::string room)
{
	//TODO: Verify room is available

	if (this->currentRoom == nullptr)
		this->currentRoom = std::make_shared<ChatRoom>(/*P2PManager*/, this->username, room);
	else
	{
		this->currentRoom.get()->close();
		this->currentRoom = std::make_shared<ChatRoom>(/*P2PManager*/, this->username, room);
	}

	return this->currentRoom;
}
#endif