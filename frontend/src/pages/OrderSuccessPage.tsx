import React from 'react';
import { CheckCircle, Package, Clock } from 'lucide-react';

interface OrderSuccessPageProps {
  onPageChange: (page: string) => void;
}

export function OrderSuccessPage({ onPageChange }: OrderSuccessPageProps) {
  const orderId = 'ORD-' + Math.random().toString(36).substr(2, 9).toUpperCase();

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center">
      <div className="max-w-md w-full mx-4">
        <div className="bg-white rounded-lg shadow-md p-8 text-center">
          <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-6">
            <CheckCircle className="h-10 w-10 text-green-600" />
          </div>
          
          <h1 className="text-2xl font-bold text-gray-900 mb-2">
            Order Placed Successfully!
          </h1>
          
          <p className="text-gray-600 mb-6">
            Thank you for your purchase. Your order has been confirmed and will be processed shortly.
          </p>
          
          <div className="bg-gray-50 rounded-lg p-4 mb-6">
            <p className="text-sm text-gray-600 mb-1">Order Number</p>
            <p className="font-mono text-lg font-semibold text-gray-900">{orderId}</p>
          </div>
          
          <div className="space-y-4 mb-8">
            <div className="flex items-center justify-center space-x-3 text-sm text-gray-600">
              <Clock className="h-4 w-4" />
              <span>Processing time: 1-2 business days</span>
            </div>
            <div className="flex items-center justify-center space-x-3 text-sm text-gray-600">
              <Package className="h-4 w-4" />
              <span>Estimated delivery: 3-5 business days</span>
            </div>
          </div>
          
          <div className="space-y-3">
            <button
              onClick={() => onPageChange('orders')}
              className="w-full bg-blue-600 text-white py-3 rounded-lg hover:bg-blue-700 transition-colors"
            >
              Track Your Order
            </button>
            <button
              onClick={() => onPageChange('products')}
              className="w-full border border-gray-300 text-gray-700 py-3 rounded-lg hover:bg-gray-50 transition-colors"
            >
              Continue Shopping
            </button>
          </div>
          
          <p className="text-xs text-gray-500 mt-6">
            You will receive an email confirmation shortly with your order details.
          </p>
        </div>
      </div>
    </div>
  );
}