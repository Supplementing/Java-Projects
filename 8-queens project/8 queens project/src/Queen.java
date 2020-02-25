/*
 * Project Name: 8-Queens Problem with Hill-CLimbing/Random Restarts
 * Author: Mason Herron
 * Date: 9-1-19
 */
import java.util.*;
public class Queen {
	
	private int row;
	private int column;
	private int hu;
	
	public Queen(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	//returns the row
	public int getRow()
	{
		return row;
	}
	//returns the column
	public int getColumn()
	{
		return column;
	}
//takes a queen object and returns if this (queen object) is in conflict with the sent in queen object
	public Boolean conflicts(Queen q)
	{
		if (row == q.getRow() || column == q.getColumn()) {
			return true;
		}
		
		//used to check if any other queens are diagonal because if the queen is the same number of rows as it is columns away, its diagonal by definition. 
		else if((Math.abs(row-q.getRow())) == (Math.abs(column-q.getColumn())) )
		{
			return true;
		}
		
		else
			return false;
	}
	//sets the hueristic field
	public void setHueristic(int h) {
		hu = h;
	}
	//returns the heuristic field
	public int getHueristic() {
		return hu;
	}
	
	//sets the row, used for making moves
	public void setRow(int r) 
	{
		this.row = r;
	}
	
}
