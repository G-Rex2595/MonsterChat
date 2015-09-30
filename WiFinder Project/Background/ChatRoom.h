/* Project:  WiFinder
 * File:  ChatRoom.h
 * Author:  Andrew Sytsma
 *
 * [INSERT DESCRIPTION]
 */

#ifndef ChatRoom_H
#define ChatRoom_H

#include <String>
#include "Message.h"

using std::string;

class ChatRoom
{
public:
	//ChatRoom(P2PManager manager);	//uses wrapped class
	virtual Message *getMessages();
	virtual void sendMessage(Message msg);
	virtual void addMessage(Message msg);
	virtual void setUsername(string username);
	virtual bool failedConnect();
	virtual void close();
	//virtual void setChatView(ChatView); //uses wrapped class
};

#endif