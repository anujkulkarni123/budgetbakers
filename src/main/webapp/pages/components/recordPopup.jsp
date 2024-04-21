<div id="recordPopup" style="display: none; position: fixed; left: 0; top: 0; width: 100%; height: 100%; overflow: auto; background-color: rgba(0, 0, 0, 0.4); z-index: 1050; display: flex; justify-content: center; align-items: center;">
    <div style="background-color: #fefefe; margin: auto; padding: 20px; border: 1px solid #888; width: 60%; box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);">
        <div style="display: flex; justify-content: space-between; align-items: center;">
            <h2 style="margin: 0;">ADD RECORD</h2>
            <span onclick="closeAddRecordPopup()" style="font-size: 28px; font-weight: bold; cursor: pointer;">&times;</span>
        </div>
        <hr style="margin-top: 10px;">
        <div style="display: flex; height: 80%;">
            <div style="width: 60%; padding-right: 20px; border-right: 1px solid #ccc;">
                <div style="margin-bottom: 10px;">
                    <button onclick="showSection('expense')">Expense</button>
                    <button onclick="showSection('income')">Income</button>
                    <button onclick="showSection('transfer')">Transfer</button>
                </div>
                <div id="expense" class="content" style="display: none;">
                    <p>Expense form content here.</p>
                </div>
                <div id="income" class="content" style="display: none;">
                    <p>Income form content here.</p>
                </div>
                <div id="transfer" class="content" style="display: none;">
                    <p>Transfer form content here.</p>
                </div>
                <button type="button" style="bottom: 20px; right: 20px;">Add Record</button>
            </div>
            <div style="width: 40%; padding-left: 20px;">
                <form>
                    <div class="mb-3">
                        <label for="payee" class="form-label">Payee</label>
                        <input type="text" id="payee" name="payee" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label for="note" class="form-label">Note</label>
                        <input type="text" id="note" name="note" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label for="paymentType" class="form-label">Payment Type</label>
                        <select id="paymentType" name="paymentType" class="form-select">
                            <option selected>Choose...</option>
                            <option value="cash">Cash</option>
                            <option value="credit_card">Credit Card</option>
                            <option value="debit_card">Debit Card</option>
                            <option value="other">Other</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="paymentStatus" class="form-label">Payment Status</label>
                        <select id="paymentStatus" name="paymentStatus" class="form-select">
                            <option selected>Choose...</option>
                            <option value="paid">Paid</option>
                            <option value="pending">Pending</option>
                            <option value="failed">Failed</option>
                        </select>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
