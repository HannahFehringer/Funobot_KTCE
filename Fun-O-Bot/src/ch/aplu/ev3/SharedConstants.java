// SharedConstants.java

/*
 This software is part of the EV3JLib library.
 It is Open Source Free Software, so you may
 - run the code for any purpose
 - study how the code works and adapt it to your needs
 - integrate all or parts of the code in your own programs
 - redistribute copies of the code
 - improve the code and release your improvements to the public
 However the use of the code is entirely your responsibility.
 */

/* History:
 * V1.00 - Jul 2014: - Ported from NxtJLibA
 * V1.01 - Jul 2014: - Tools.wait<button> now triggered by press (and not press&release)
 * V1.02 - Jul 2014: - Added: ColorSensor.getColorLabel() and ColorCubes
 * V1.03 - Jul 2014: - Modified: ColorLabel enum in package root
 * V1.04 - Aug 2014: - Fixed: GenericMotor.stop() now sets STOP flag
                     - Fixed: Tools.startButtonListener() now public
 * V1.05 - Aug 2014  - Added: ButtonListener to support the event model
 * V1.06 - Aug 2014  - Added: RemoteListener to support IRRemoteSensor events
 * V1.07 - Aug 2014  - Added: ColorSensor.getColorStr()
 *                   - Modified: UltrasonicSensor, NxtUltrasonicSensor now returns 255 
 *                     if no target is detected
 */
package ch.aplu.ev3;

/**
 * Declarations of important global constants.
 */
interface SharedConstants
{
  int DEBUG_LEVEL_OFF = 0;    // Elementary debug info
  int DEBUG_LEVEL_LOW = 1;    // Debug info when threads start/stop
  int DEBUG_LEVEL_MEDIUM = 2; // Debug info for important method calls its paramaters

  String ABOUT
    = "2003-2014 Aegidius Pluess\n"
    + "OpenSource Free Software\n"
    + "http://www.aplu.ch\n"
    + "All rights reserved";
  String VERSION = "1.07 - Aug 2014";
}
