package com.together.vo;

public class HeartVO {
	private String userID;
	private int boardID;

	public HeartVO() {
	}

	public HeartVO(String userID, int boardID) {
		this.userID = userID;
		this.boardID = boardID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public int getBoardID() {
		return boardID;
	}

	public void setBoardID(int boardID) {
		this.boardID = boardID;
	}

	@Override
	public String toString() {
		return "HeartVO [userID=" + userID + ", boardID=" + boardID + "]";
	}

}
