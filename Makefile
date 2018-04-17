default:
	javac -d bin src/*.java src/algebraicPetriNet/*.java
	cd bin; jar cfe NOPSI.jar NOPSI NOPSI.class algebraicPetriNet/*.class
