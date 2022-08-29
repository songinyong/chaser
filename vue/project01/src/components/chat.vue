<template>
    <div class="chat">
        <h3>채팅창</h3>
        <textarea id="chatMsg" name="chatMsg" rows="20"></textarea>
        <input type="text" id="chatInput"  maxlength='50'/>
        <button @click="sendChat()">채팅보내기</button>

    </div>

</template>

<script>
export default {
    name: 'chatComponent',
    setup() {
        

    },
}
</script>
