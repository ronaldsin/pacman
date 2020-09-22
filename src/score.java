
public class score {

	public score(int score){//updates the score label
		String scoreStr =Integer.toString(score);
		while(scoreStr.length()<7){
			scoreStr= "0"+scoreStr;
		}
		PacManGui.score.setText(scoreStr);
	}
}
