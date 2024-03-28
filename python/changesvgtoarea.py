# svg->area
with open("../python/text.txt") as f:
    lines = f.readlines()
linesAfter = []
for line in lines:
    linesAfter.append(line.strip().split(","))
linesOk = []
for s in linesAfter:
    skok=[0,0,0,0]
    sktemp = s
    skok[0]=sktemp[0]
    skok[1]=sktemp[1]
    skok[2]=str(int(sktemp[0])+int(sktemp[2]))
    skok[3]=str(int(sktemp[1])+int(sktemp[3]))
    linesOk.append(skok)
for k in linesOk:
    print(k[0]+","+k[1]+","+k[2]+","+k[3])