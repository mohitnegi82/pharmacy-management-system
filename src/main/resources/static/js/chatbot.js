// Simple rule-based platform-assistant chatbot (frontend only, no AI API / no cost).
// Purpose: help the pharmacist navigate the PMS platform (NOT medical advice / NOT OTC suggestions).

const chatRules = [
    { keywords: ["add medicine", "new medicine", "create medicine"],
      reply: "Go to Medicine Management → click 'Add New Medicine', fill the form, and click Save." },
    { keywords: ["search medicine", "find medicine"],
      reply: "Use the search bar on the Medicines page — type a medicine name to filter results." },
    { keywords: ["update medicine", "edit medicine"],
      reply: "On the Medicines page, click 'Edit' next to any medicine to update its details." },
    { keywords: ["delete medicine", "remove medicine"],
      reply: "On the Medicines page, click 'Delete' next to a medicine. This action cannot be undone." },
    { keywords: ["supplier"],
      reply: "Supplier Management lets you list, add, or delete suppliers. Find it in the top navbar." },
    { keywords: ["purchase", "stock in"],
      reply: "Go to 'Purchase' in the navbar, pick a supplier, select medicines and quantities, then Save — stock quantities update automatically." },
    { keywords: ["sale", "checkout", "sell", "invoice"],
      reply: "Go to 'Checkout', add medicines to the cart with quantity, enter customer details, and submit — an invoice is generated automatically." },
    { keywords: ["otc", "over the counter", "age group", "dosage", "symptom", "disease"],
      reply: "The OTC Lookup tool suggests top 3 basic (non-prescription) medicines based on patient age and symptom category. Note: this is a reference aid only — final decision is always the pharmacist's." },
    { keywords: ["message", "inbox"],
      reply: "Messages lets you view your inbox or send a message to another staff member from the navbar." },
    { keywords: ["report", "low stock", "expiry", "earnings"],
      reply: "Reports shows stock summary, low stock alerts, items expiring in 30 days, and today's sales/earnings." },
    { keywords: ["employee", "staff"],
      reply: "Employee Management (Admin only) lets you list, add, or deactivate employees." },
    { keywords: ["login", "password", "logout"],
      reply: "Use your pharmacist username/password to log in. Click Logout in the top-right navbar to sign out." },
    { keywords: ["hello", "hi", "hey"],
      reply: "Hi! I'm the PharmaCare platform assistant. Ask me how to use any feature — e.g. 'how to record a sale'." }
];

const fallbackReply = "I can help you navigate PharmaCare (medicines, suppliers, purchases, sales, OTC lookup, reports, employees). Try asking something like 'how do I add a medicine?'";

function toggleChatbot() {
    const win = document.getElementById('chatbot-window');
    win.classList.toggle('open');
    if (win.classList.contains('open') && document.getElementById('chatbot-messages').children.length === 0) {
        addChatMessage("bot", "Hi! I'm your PharmaCare platform assistant. Ask me how to use any feature.");
    }
}

function addChatMessage(sender, text) {
    const container = document.getElementById('chatbot-messages');
    const div = document.createElement('div');
    div.className = 'chat-msg ' + sender;
    div.textContent = text;
    container.appendChild(div);
    container.scrollTop = container.scrollHeight;
}

function getBotReply(userText) {
    const lower = userText.toLowerCase();
    for (const rule of chatRules) {
        if (rule.keywords.some(k => lower.includes(k))) {
            return rule.reply;
        }
    }
    return fallbackReply;
}

function sendChatMessage() {
    const input = document.getElementById('chatbot-input');
    const text = input.value.trim();
    if (!text) return;
    addChatMessage("user", text);
    input.value = "";
    setTimeout(() => addChatMessage("bot", getBotReply(text)), 300);
}
