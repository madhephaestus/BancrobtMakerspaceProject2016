import com.neuronrobotics.bowlerstudio.creature.ICadGenerator;
import com.neuronrobotics.bowlerstudio.creature.CreatureLab;
import org.apache.commons.io.IOUtils;
import com.neuronrobotics.bowlerstudio.vitamins.*;
import com.neuronrobotics.bowlerstudio.physics.*;

Closure generateServo = (Closure)ScriptingEngine
					 .gitScriptRun(
            "https://github.com/madhephaestus/BancroftMakerspaceProject2016.git", // git location of the library
            "vexServo.groovy" , // file to load
            null// no parameters (see next tutorial)
            );         
	/**
 * Gets the all dh chains.
 *
 * @return the all dh chains
 */
public ArrayList<DHParameterKinematics> getLimbDHChains(MobileBase base) {

	return base.getAllDHChains();
}

// Load an STL file from a git repo
// Loading a local file also works here
		CSG dyioReference=   (CSG)(ScriptingEngine.inlineGistScriptRun(
		"fb4cf429372deeb36f52", 
		"dyioCad.groovy" ,
		null))
	
//	.transformed(new Transform().translateZ(12.0))
//	.transformed(new Transform().translateX(5.4));
	
	//CSG horn=  STL.file(NativeResource.inJarLoad(IVitamin.class,"smallmotorhorn.stl").toPath())
	CSG horn = new Cube(6,5,12).toCSG();
	CSG hornBlank = new Cube(6,5,12).toCSG()
			.toZMax()
			.toYMin()
			.movez(4)
	CSG mountReference = (CSG)(ScriptingEngine.inlineGistScriptRun(
		"ce4e7c95d516e265b91e",
		"servoAttachment.groovy" ,
		[hornBlank]))
	CSG mountScrewKeepaway = (CSG)(ScriptingEngine.inlineGistScriptRun(
		"488e0ee249a5c16ae4d8",
		"moutScrewKeepaway.groovy" ,
		null))
// Load the .CSG from the disk and cache it in memory
//CSG servo  = Vitamins.get(servoFile);
return new ICadGenerator(){
	@Override 
	public ArrayList<CSG> generateCad(DHParameterKinematics d, int linkIndex) {
		ArrayList<CSG> allCad=new ArrayList<>()

		return allCad;
	}
	//This is the keep-away shape for the pulling out of the servo
	private CSG getAppendageMount(){
		
		double keepAwayHeight = 16;

		
		CSG tmp =generateServo(200)
				.rotz(90)
				
		CSG cylinder = new Cylinder(	20, // Radius at the top
                      				20, // Radius at the bottom
                      				keepAwayHeight, // Thickness of the leg part
                      			         (int)20 //resolution
                      			         ).toCSG()
                      			         .movex(10)
           CSG setScrew = new Cylinder(	5/2, // Radius at the top
                      				5/2, // Radius at the bottom
                      				200, // Thickness of the leg part
                      			         (int)20 //resolution
                      			         ).toCSG().roty(90)
                      			          .movez(-15/2)		         
                      			     
		return tmp
		.union(
			cylinder.movez(-keepAwayHeight - 1),
			tmp.movez(keepAwayHeight), setScrew
			)
		
	}
	private CSG getAttachment(){
		CSG cylinder = new Cylinder(	20, // Radius at the top
                      				20, // Radius at the bottom
                      				1, // Thickness of the leg part
                      			         (int)20 //resolution
                      			         ).toCSG()
                      			         .movex(10)
                      			         .movez(18)
		return generateServo(20)
				.rotz(90)
				.union(cylinder)

	}
	@Override 
	public ArrayList<CSG> generateBody(MobileBase b ) {
		ArrayList<CSG> allCad=new ArrayList<>();
		ArrayList<CSG> cutouts=new ArrayList<>();
		ArrayList<CSG> attach=new ArrayList<>();
		CSG attachUnion=null;
		CSG tmp = generateServo(25);
		for(DHParameterKinematics l:b.getLegs()){
			
			TransformNR position = l.getRobotToFiducialTransform();
			Transform csgTrans = TransformFactory.nrToCSG(position)
			cutouts.add(getAppendageMount()
						
				.transformed(csgTrans)
				.setColor(javafx.scene.paint.Color.CYAN)
				)
				
			CSG attachment = getAttachment()
				.transformed(csgTrans)
			attach.add(attachment);
			if(attachUnion==null){
				attachUnion=attachment;
			}else{
				attachUnion = 	attachUnion.union(attachment)
			}
			
			
		}

		CSG upperBody = attachUnion
		.hull()
		
						
		CSG myDyIO=dyioReference
				.movez(upperBody.getMaxZ())
				.movex(	upperBody.getMaxX()-
						Math.abs(upperBody.getMinX()) / 2)
				.movey(	upperBody.getMaxY()-
						Math.abs(upperBody.getMinY()) / 2)
		upperBody=upperBody
		.union(myDyIO)
		.hull()
		.difference(myDyIO)
		
		upperBody= upperBody.difference(cutouts);
		
		//upperBody= upperBody.union(attach);
		
	
					
		
		upperBody.setManipulator(b.getRootListener());
		upperBody.setManufactuing(new PrepForManufacturing() {
					public CSG prep(CSG arg0) {
						return arg0.toZMin();
					}
				});
		allCad.add(upperBody)
		
		//return cutouts
		return allCad;
	}
};
