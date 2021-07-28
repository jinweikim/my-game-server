package com.mygame.game.message;

public enum GameMessageCode {
	M210(210,"购买竞技场挑战次数"),
	M211(211,""),
	;
	private int messageId;
	private String desc;
	private GameMessageCode(int messageId, String desc) {
		this.messageId = messageId;
		this.desc = desc;
	}
	public int getMessageId() {
		return messageId;
	}
	public String getDesc() {
		return desc;
	}
	
	
}
