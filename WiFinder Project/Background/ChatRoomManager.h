#ifndef CHATROOMMANAGER_H
#define CHATROOMMANAGER_H

#include<string>
#include<memory>
#include<vector>
#include"ChatRoom.h"

class ChatRoomManager final
{
public:
	ChatRoomManager(std::string);
	std::vector<std::string> getAvailableRooms();
	void setUsername(std::string);
	std::shared_ptr<ChatRoom> joinRoom(std::string);
private:
	std::string username;
	std::shared_ptr<ChatRoom> currentRoom;
};
#endif