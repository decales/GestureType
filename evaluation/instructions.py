base = ("one two three four five six seven eight nine ten eleven twelve "
        "thirteen fourteen fifteen sixteen seventeen eighteen nineteen twenty")

ins1 = ("Insert 'APPLE' after 'eight'", "Word insertion", 39)
text1 = ("one two three four five six seven eight APPLE nine ten eleven twelve "
         "thirteen fourteen fifteen sixteen seventeen eighteen nineteen twenty")

ins2 = ("Delete 'i' from 'nineteen'", "Character deletion", 79)
text2 = ("one two three four five six seven eight APPLE nine ten eleven twelve "
         "thirteen fourteen fifteen sixteen seventeen eighteen nneteen twenty")

ins3 = ("Insert 'BANANA' after 'fifteen'", "Word insertion", 29)
text3 = ("one two three four five six seven eight APPLE nine ten eleven twelve "
         "thirteen fourteen fifteen BANANA sixteen seventeen eighteen nneteen twenty")

ins4 = ("Prepend 'D' to 'fourteen'", "Character insertion", 16)
text4 = ("one two three four five six seven eight APPLE nine ten eleven twelve "
         "thirteen Dfourteen fifteen BANANA sixteen seventeen eighteen nneteen twenty")

ins5 = ("Select and cut 'APPLE'", "Select/cut", 39)
text5 = ("one two three four five six seven eight  nine ten eleven twelve "
         "thirteen Dfourteen fifteen BANANA sixteen seventeen eighteen nneteen twenty")

ins6 = ("Paste the buffer after 'BANANA'", "Paste", 57)
text6 = ("one two three four five six seven eight  nine ten eleven twelve "
         "thirteen Dfourteen fifteen BANANA APPLE sixteen seventeen eighteen nneteen twenty")

ins7 = ("Append 'X' to 'thirteen'", "Character insertion", 31)
text7 = ("one two three four five six seven eight  nine ten eleven twelve "
         "thirteenX Dfourteen fifteen BANANA APPLE sixteen seventeen eighteen nneteen twenty")

ins8 = ("Select and cut 'thirteenX Dfourteen'", "Select/Cut", 9)
text8 = ("one two three four five six seven eight  nine ten eleven twelve "
         " fifteen BANANA APPLE sixteen seventeen eighteen nneteen twenty")

ins9 = ("Paste the buffer before 'one'", "Paste", 65)
text9 = ("thirteenX Dfourteen one two three four five six seven eight  nine ten eleven twelve "
         " fifteen BANANA APPLE sixteen seventeen eighteen nneteen twenty")

ins10 = ("Prepend 'A' to 'twenty'", "Character insertion", 120)
text10 = ("thirteenX Dfourteen one two three four five six seven eight  nine ten eleven twelve "
          " fifteen BANANA APPLE sixteen seventeen eighteen nneteen Atwenty")

ins11 = ("Append '.' to 'ten'", "Character insertion", 74)
text11 = ("thirteenX Dfourteen one two three four five six seven eight  nine ten. eleven twelve "
          " fifteen BANANA APPLE sixteen seventeen eighteen nneteen Atwenty")

ins12 = ("Delete 'x' from 'sixteen'", "Character deletion", 39)
text12 = ("thirteenX Dfourteen one two three four five six seven eight  nine ten. eleven twelve "
          " fifteen BANANA APPLE siteen seventeen eighteen nneteen Atwenty")

ins13 = ("Append ',' to 'thirteenX'", "Character insertion", 99)
text13 = ("thirteenX, Dfourteen one two three four five six seven eight  nine ten. eleven twelve "
          " fifteen BANANA APPLE siteen seventeen eighteen nneteen Atwenty")

ins14 = ("Select and cut 'one two three'", "Select/Cut", 11)
text14 = ("thirteenX, Dfourteen  four five six seven eight  nine ten. eleven twelve "
          " fifteen BANANA APPLE siteen seventeen eighteen nneteen Atwenty")

ins15 = ("Paste the buffer after 'Atwenty'", "Paste", 115)
text15 = ("thirteenX, Dfourteen  four five six seven eight  nine ten. eleven twelve "
          " fifteen BANANA APPLE siteen seventeen eighteen nneteen Atwenty one two three")

ins16 = ("Select and cut 'four five six seven eight'", "Select/Cut", 128)
text16 = ("thirteenX, Dfourteen    nine ten. eleven twelve "
          " fifteen BANANA APPLE siteen seventeen eighteen nneteen Atwenty one two three")

ins17 = ("Paste the buffer after 'three'", "Paste", 103)
text17 = ("thirteenX, Dfourteen    nine ten. eleven twelve "
          " fifteen BANANA APPLE siteen seventeen eighteen nneteen Atwenty one two three four five six seven eight")

ins18 = ("Insert 'BLUEBERRY' after 'APPLE'", "Word insertion", 82)
text18 = ("thirteenX, Dfourteen    nine ten. eleven twelve "
          " fifteen BANANA APPLE BLUEBERRY siteen seventeen eighteen nneteen Atwenty one two three four five six seven eight")

ins19 = ("Delete 'o' from 'one'", "Character deletion", 44)
text19 = ("thirteenX, Dfourteen    nine ten. eleven twelve "
          " fifteen BANANA APPLE BLUEBERRY siteen seventeen eighteen nneteen Atwenty ne two three four five six seven eight")

ins20 = ("Select and cut 'A' from 'APPLE'", "Select/Cut", 58)
text20 = ("thirteenX, Dfourteen    nine ten. eleven twelve "
          " fifteen BANANA PPLE BLUEBERRY siteen seventeen eighteen nneteen Atwenty ne two three four five six seven eight")

ins21 = ("Prepend the buffer to 'nneteen'", "Paste", 41)
text21 = ("thirteenX, Dfourteen    nine ten. eleven twelve "
          " fifteen BANANA PPLE BLUEBERRY siteen seventeen eighteen Anneteen Atwenty ne two three four five six seven eight")

ins22 = ("Insert 'PEAR' after 'BANANA'", "Word insertion", 43)
text22 = ("thirteenX, Dfourteen    nine ten. eleven twelve "
          " fifteen BANANA PEAR PPLE BLUEBERRY siteen seventeen eighteen Anneteen Atwenty ne two three four five six seven eight")

ins23 = ("Delete 'P' from 'Pear'", "character deletion", 3)
text23 = ("thirteenX, Dfourteen    nine ten. eleven twelve "
          " fifteen BANANA EAR PPLE BLUEBERRY siteen seventeen eighteen Anneteen Atwenty ne two three four five six seven eight")

ins24 = ("Select and cut 'BANANA EAR PPLE BLUEBERRY'", "Select/Cut", 7)
text24 = ("thirteenX, Dfourteen    nine ten. eleven twelve "
          " fifteen  siteen seventeen eighteen Anneteen Atwenty ne two three four five six seven eight")

ins25 = ("Paste the buffer after 'eight'", "Paste", 83)
text25 = ("thirteenX, Dfourteen    nine ten. eleven twelve "
          " fifteen  siteen seventeen eighteen Anneteen Atwenty ne two three four five six seven eight BANANA EAR PPLE BLUEBERRY")

text = [text1, text2, text3, text4, text5, text6, text7, text8, text9, text10, text11, text12,
        text13, text14, text15, text16, text17, text18, text19, text20, text21, text22, text23, text24, text25]

instructions = [ins1, ins2, ins3, ins4, ins5, ins6, ins7, ins8, ins9, ins10, ins11, ins12, ins13,
                ins14, ins15, ins16, ins17, ins18, ins19, ins20, ins21, ins22, ins23, ins24, ins25, ("", "Done", 0)]
