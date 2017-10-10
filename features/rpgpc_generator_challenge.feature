Feature: Problem Set 04, RPG PC Generator Challenge

    Scenario Outline: Desired output from input without errors
        Given rolls of <rollOne>, <rollTwo>, <rollThree>, and <rollFour>
        And a race of <race>
        When I run the RPG PC Generator Challenge
        Then I should see the race displayed as <raceDisplay>
        And the dexterity score displayed as <dexScore>
        And the dexterity modifier displayed as <dexModifier>
        And the note should be <note>

    Examples:
    | race  | rollOne   | rollTwo   | rollThree | rollFour  | raceDisplay   | dexScore  | dexModifier   | note                                  |
    | "M"   | 1         | 1         | 1         | 1         | "HUMAN"       | "4*"      | "-3"          | "Note: applied racial modifier (+1)"  |
    | "m"   | 1         | 1         | 1         | 1         | "HUMAN"       | "4*"      | "-3"          | "Note: applied racial modifier (+1)"  |
    | "D"   | 1         | 1         | 1         | 1         | "DWARF"       | "3"       | "-4"          | ""                                    |
    | "H"   | 6         | 5         | 1         | 6         | "HALFLING"    | "19*"     | "+4"          | "Note: applied racial modifier (+2)"  |
    | "d"   | 3         | 3         | 3         | 4         | "DWARF"       | "10"      | "+0"          | ""                                    |
    | "h"   | 1         | 2         | 2         | 2         | "HALFLING"    | "8*"      | "-1"          | "Note: applied racial modifier (+2)"  |
    | "e"   | 2         | 3         | 4         | 4         | "ELF"         | "13*"     | "+1"          | "Note: applied racial modifier (+2)"  |
    | "m"   | 1         | 1         | 2         | 2         | "HUMAN"       | "6*"      | "-2"          | "Note: applied racial modifier (+1)"  |
    | "d"   | 3         | 1         | 1         | 1         | "DWARF"       | "5"       | "-3"          | ""                                    |
    | "d"   | 3         | 2         | 1         | 2         | "DWARF"       | "7"       | "-2"          | ""                                    |
    | "d"   | 4         | 3         | 2         | 2         | "DWARF"       | "9"       | "-1"          | ""                                    |
    | "d"   | 4         | 3         | 4         | 3         | "DWARF"       | "11"      | "+0"          | ""                                    |
    | "d"   | 3         | 5         | 3         | 4         | "DWARF"       | "12"      | "+1"          | ""                                    |
    | "d"   | 5         | 5         | 3         | 4         | "DWARF"       | "14"      | "+2"          | ""                                    |
    | "d"   | 6         | 6         | 3         | 3         | "DWARF"       | "15"      | "+2"          | ""                                    |
    | "d"   | 3         | 6         | 5         | 5         | "DWARF"       | "16"      | "+3"          | ""                                    |
    | "d"   | 3         | 6         | 6         | 5         | "DWARF"       | "17"      | "+3"          | ""                                    |
    | "d"   | 6         | 6         | 6         | 6         | "DWARF"       | "18"      | "+4"          | ""                                    |