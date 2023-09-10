
const Mode = Packet.com.dakuo.craftrecycle.ui.slotmode.Mode
const Clicked = Packet.com.dakuo.craftrecycle.ui.slotmode.clicked.Clicked
const collectionUtils = Packet.com.dakuo.craftrecycle.scripts.utils.CollectionUtils.INSTANCE
const modeLoader = Packet.com.dakuo.craftrecycle.ui.slotmode.ModeLoader


const example = Java.extend(Mode, {
    name: collectionUtils.listOf("example"),
    clicked: Java.extend(Clicked, {
        click: function (clickEvent) {
            print("这是一条由javascrpit发送的消息")
        }
    })
});

modeLoader.register(example)